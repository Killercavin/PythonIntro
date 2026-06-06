package io.github.devcavin.usageservice.service

import io.github.devcavin.kafka.event.EnergyUsageEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class UsageService {
    private val log = LoggerFactory.getLogger(UsageService::class.java)

    @KafkaListener(topics = ["wattwise-energy-usage"], groupId = "wattwise-usage-service")
    fun energyUsageEvent(energyUsageEvent: EnergyUsageEvent) {
        log.info("Received energy usage event: $energyUsageEvent")
    }
}