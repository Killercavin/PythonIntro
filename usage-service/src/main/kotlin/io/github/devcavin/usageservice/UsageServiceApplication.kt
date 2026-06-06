package io.github.devcavin.usageservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UsageServiceApplication

fun main(args: Array<String>) {
	runApplication<UsageServiceApplication>(*args)
}
