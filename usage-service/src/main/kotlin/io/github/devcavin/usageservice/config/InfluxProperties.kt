package io.github.devcavin.usageservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "influx")
data class InfluxProperties(
    val influxUrl: String,
    val influxToken: String,
    val influxOrganization: String,
    val influxBucket: String
)
