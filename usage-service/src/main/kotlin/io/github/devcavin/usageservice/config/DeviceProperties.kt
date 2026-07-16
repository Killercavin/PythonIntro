package io.github.devcavin.usageservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("device")
data class DeviceProperties(
    val deviceUrl: String
)
