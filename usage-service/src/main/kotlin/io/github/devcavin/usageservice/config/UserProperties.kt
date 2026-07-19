package io.github.devcavin.usageservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("user.service")
data class UserProperties(
    val userUrl: String
)
