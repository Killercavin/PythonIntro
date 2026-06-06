package io.github.devcavin.kafka.event

import java.time.Instant

data class EnergyUsageEvent(
    val deviceId: Long,
    val energyConsumed: Double,
    val createdAt: Instant = Instant.now()
)
