package io.github.devcavin.usageservice.service

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.QueryApi
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import com.influxdb.query.FluxTable
import io.github.devcavin.kafka.event.AlertingEvent
import io.github.devcavin.kafka.event.EnergyUsageEvent
import io.github.devcavin.usageservice.client.DeviceClient
import io.github.devcavin.usageservice.client.UserClient
import io.github.devcavin.usageservice.config.InfluxProperties
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
    private val properties: InfluxProperties,
    private val deviceClient: DeviceClient,
    private val userClient: UserClient,
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

        log.info("Aggregated device energies over the past hour: {}", deviceEnergies)

        val updatedDeviceEnergies = deviceEnergies.map { deviceEnergy ->
            try {
                val deviceResponse = deviceClient.getDeviceById(deviceEnergy.deviceId)

                deviceEnergy.copy(userId = deviceResponse.userId)
            } catch (e: Exception) {
                log.warn("Failed to fetch device for ID: {}", deviceEnergy.deviceId)
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
                    log.info("ALERT: User ID {} has exceeded the energy threshold! " +
                            "Total Consumption: {}, Threshold: {}",
                        userId, totalConsumption, threshold)

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
                    log.info("User ID {} is within the energy threshold. " +
                            "Total Consumption: {}, Threshold: {}",
                        userId, totalConsumption, threshold)
                }
            }
    }
}