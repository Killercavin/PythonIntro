package io.github.devcavin.usageservice.service

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.QueryApi
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import com.influxdb.query.FluxTable
import io.github.devcavin.kafka.event.AlertingEvent
import io.github.devcavin.kafka.event.EnergyUsageEvent
import io.github.devcavin.usageservice.client.DeviceClient
import io.github.devcavin.usageservice.config.UsageAppProperties
import io.github.devcavin.usageservice.dto.DeviceResponse
import io.github.devcavin.usageservice.dto.UsageDto
import io.github.devcavin.usageservice.dto.UserConsumption
import io.github.devcavin.usageservice.model.DeviceEnergy
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class UsageService(
    private val influxDBClient: InfluxDBClient,
    private val properties: UsageAppProperties,
    private val deviceClient: DeviceClient,
    private val kafkaTemplate: KafkaTemplate<String, AlertingEvent>
) {
    private val log = LoggerFactory.getLogger(UsageService::class.java)

    @KafkaListener(topics = ["wattwise-energy-usage"], groupId = "wattwise-usage-service")
    fun energyUsageEvent(energyUsageEvent: EnergyUsageEvent) {
        log.info("Received energy usage event: $energyUsageEvent")

        val point: Point = Point.measurement("energy-usage")
            .addTag("deviceId", energyUsageEvent.deviceId.toString())
            .addField("energy-consumed", energyUsageEvent.energyConsumed)
            .time(energyUsageEvent.createdAt, WritePrecision.MS)

        influxDBClient.writeApiBlocking.writePoint(
            properties.influxBucket,
            properties.influxOrganization,
            point)
    }

    @Scheduled(cron = "*/10 * * * * *")
    fun aggregateDeviceEnergyUsage() {
        val now = Instant.now()
        val oneHourAgo = now.minusSeconds(3600)

        val fluxQuery = """
            from(bucket: "${properties.influxBucket}")
            |> range(start: time(v: "$oneHourAgo"), stop: time(v: "$now"))
            |> filter(fn: (r) => r["_measurement"] == "energy-usage")
            |> filter(fn: (r) => r["_field"] == "energy-consumed")
            |> group(columns: ["deviceId"])
            |> sum(column: "_value")
            """.trimIndent()

        val queryApi: QueryApi = influxDBClient.queryApi

        val tables: List<FluxTable> = queryApi.query(fluxQuery, properties.influxOrganization)

        val deviceEnergies = tables.flatMap { table ->
            table.records.map { record ->
                DeviceEnergy(
                    deviceId = (record.getValueByKey("deviceId") as String).toLong(),
                    energyConsumed = (record.getValueByKey("_value") as? Number)?.toDouble() ?: 0.0,
                    userId = (record.getValueByKey("userId") as String).toLong()
                )
            }
        }

        log.info("Aggregated device energies over the past hour: $deviceEnergies")

        val updatedDeviceEnergies = deviceEnergies.map { deviceEnergy ->
            try {
                val deviceResponse = deviceClient.getDeviceById(deviceEnergy.deviceId)

                deviceEnergy.copy(userId = deviceResponse.userId)
            } catch (e: Exception) {
                log.warn("Failed to fetch device for ID: ${deviceEnergy.deviceId}")
                deviceEnergy
            }
        }

        val userDeviceEnergyMap: Map<Long, List<DeviceEnergy>> = deviceEnergies.groupBy { it.userId }
        log.info("User-Device Energy Map: {}", userDeviceEnergyMap)


        val userIds: List<Long> = userDeviceEnergyMap.keys.toList()

        val userThresholdMap: Map<Long, Double> = hashMapOf()
        val userEmailMap: Map<Long, String> = hashMapOf()

        // Check thresholds against aggregated usage
        userThresholdMap.keys
            .mapNotNull { userId ->
                val threshold = userThresholdMap[userId] ?: return@mapNotNull null
                val devices = userDeviceEnergyMap[userId] ?: return@mapNotNull null
                UserConsumption(userId, threshold, devices.sumOf(DeviceEnergy::energyConsumed))
            }
            .forEach { (userId, threshold, totalConsumption) ->
                if (totalConsumption > threshold) {
                    log.info("ALERT: User ID $userId has exceeded the energy threshold! " +
                            "Total Consumption: $totalConsumption, Threshold: $threshold")

                    // Put message on kafka alert-topic
                    kafkaTemplate.send(
                        "wattwise-energy-alert",
                        AlertingEvent(
                            userId = userId,
                            message = "Energy consumption threshold exceeded",
                            threshold = threshold,
                            energyConsumed = totalConsumption,
                            email = userEmailMap[userId] ?: return@forEach
                        )
                    )
                } else {
                    log.info("User ID $userId is within the energy threshold. " +
                            "Total Consumption: $totalConsumption, Threshold: $threshold")
                }
            }
    }

    fun getXDaysUsageForUser(userId: Long, days: Int): UsageDto {
        log.info("Getting usage for user id: $userId over the past $days days")
        val devices = deviceClient.getAllDevicesForUser(userId)

        val deviceIdStringList = devices
            .map { it.id.toString() }
            .filter { it.isNotEmpty() }

        val now = Instant.now()
        val start = now.minusSeconds((days * 24 * 3600).toLong())

        val deviceFilter = deviceIdStringList
            .joinToString(" or ") { """r["deviceId"] == "$it"""" }

        val fluxQuery = """
        from(bucket: "${properties.influxBucket}")
          |> range(start: time(v: "$start"), stop: time(v: "$now"))
          |> filter(fn: (r) => r["_measurement"] == "energy_usage")
          |> filter(fn: (r) => r["_field"] == "energyConsumed")
          |> filter(fn: (r) => $deviceFilter)
          |> group(columns: ["deviceId"])
          |> sum(column: "_value")
          """.trimIndent()

        val aggregatedMap = mutableMapOf<Long, Double>()

        try {
            val queryApi = influxDBClient.queryApi
            val tables = queryApi.query(fluxQuery, properties.influxOrganization)

            tables.forEach { table ->
                table.records.forEach { record ->
                    val deviceIdStr = record.getValueByKey("deviceId")?.toString()
                    val energyConsumed = (record.getValueByKey("_value") as? Number)?.toDouble() ?: 0.0

                    deviceIdStr?.toLongOrNull()?.let { deviceId ->
                        aggregatedMap.merge(deviceId, energyConsumed, Double::plus)
                    } ?: log.warn("Failed to parse deviceId from flux record: $deviceIdStr")
                }
            }
        } catch (e: Exception) {
            log.error("Failed to query InfluxDB for user $userId usage over $days days: ${e.message}")
            devices.forEach { it.energyConsumed = 0.0 }
        }

        devices.forEach { device ->
            device.id.let { id ->
                device.energyConsumed = aggregatedMap[id] ?: 0.0
            }
        }

        log.info("Aggregated energy consumption for userId $userId: $aggregatedMap")

        val resultDevices = devices.map { device ->
            DeviceResponse(
                id = device.id,
                name = device.name,
                type = device.type,
                location = device.location,
                userId = device.userId,
                energyConsumed = device.energyConsumed
            )
        }

        return UsageDto(userId = userId, devices = resultDevices)
    }
}