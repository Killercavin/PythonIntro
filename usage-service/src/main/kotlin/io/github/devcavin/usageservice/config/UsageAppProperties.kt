package io.github.devcavin.usageservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "usage-app")
data class UsageAppProperties(
    val deviceUrl: String,
    val userUrl: String,
    val influxUrl: String,
    val influxToken: String,
    val influxOrganization: String,
    val influxBucket: String
)
