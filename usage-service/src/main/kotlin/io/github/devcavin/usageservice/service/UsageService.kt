package io.github.devcavin.usageservice.service

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import io.github.devcavin.kafka.event.EnergyUsageEvent
import io.github.devcavin.usageservice.dto.InfluxProperties
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

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
            .addField("energy-consumed", energyUsageEvent.energyConsumed.toString())
            .time(energyUsageEvent.createdAt, WritePrecision.MS)

        influxDBClient.writeApiBlocking.writePoint(
            properties.influxBucket,
            properties.influxOrganization,
            point)
    }
}