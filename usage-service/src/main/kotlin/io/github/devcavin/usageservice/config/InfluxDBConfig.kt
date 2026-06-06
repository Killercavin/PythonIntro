package io.github.devcavin.usageservice.config

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfluxDBConfig {

    @Bean
    fun influxdbClient(): InfluxDBClient {
        return InfluxDBClientFactory.create()
    }
}