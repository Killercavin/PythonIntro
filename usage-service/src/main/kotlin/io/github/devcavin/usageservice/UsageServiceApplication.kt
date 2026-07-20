package io.github.devcavin.usageservice

import io.github.devcavin.usageservice.config.DeviceProperties
import io.github.devcavin.usageservice.config.InfluxProperties
import io.github.devcavin.usageservice.config.UserProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(InfluxProperties::class, DeviceProperties::class, UserProperties::class)
@EnableScheduling
class UsageServiceApplication

fun main(args: Array<String>) {
	runApplication<UsageServiceApplication>(*args)
}
