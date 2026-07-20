package io.github.devcavin.kafka.event

data class AlertingEvent(
    val userId: Long,
    val message: String,
    val threshold: Double,
    val energyConsumed: Double,
    val email: String
)
