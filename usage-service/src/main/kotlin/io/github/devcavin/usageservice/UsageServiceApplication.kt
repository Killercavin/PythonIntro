package io.github.devcavin.usageservice

import io.github.devcavin.usageservice.config.DeviceProperties
import io.github.devcavin.usageservice.config.InfluxProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(InfluxProperties::class, DeviceProperties::class)
class UsageServiceApplication

fun main(args: Array<String>) {
	runApplication<UsageServiceApplication>(*args)
}
