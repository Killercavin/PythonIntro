package io.github.devcavin.usageservice

import io.github.devcavin.usageservice.config.UsageAppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(UsageAppProperties::class)
@EnableScheduling
class UsageServiceApplication

fun main(args: Array<String>) {
	runApplication<UsageServiceApplication>(*args)
}
