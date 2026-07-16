package io.github.devcavin.usageservice.service

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.QueryApi
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import com.influxdb.query.FluxTable
import io.github.devcavin.kafka.event.EnergyUsageEvent
import io.github.devcavin.usageservice.config.InfluxProperties
import io.github.devcavin.usageservice.model.DeviceEnergy
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UsageService(
    private val influxDBClient: InfluxDBClient,
    private val properties: InfluxProperties
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
                    energyConsumed = (record.getValueByKey("_value") as? Number)?.toDouble() ?: 0.0
                )
            }
        }

        log.info("Aggregated device energies over the past hour: {}", deviceEnergies)
    }
}