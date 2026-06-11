package io.github.devcavin.usageservice.config

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import io.github.devcavin.usageservice.dto.InfluxProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfluxDBConfig(private val properties: InfluxProperties) {
    @Bean
    fun influxdbClient(): InfluxDBClient {
        return InfluxDBClientFactory.create(
            properties.influxUrl,
            properties.influxToken.toCharArray(),
            properties.influxOrganization)
    }
}