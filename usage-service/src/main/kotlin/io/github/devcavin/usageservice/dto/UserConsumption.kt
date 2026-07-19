package io.github.devcavin.usageservice.dto

data class UserConsumption(
    val userId: Long,
    val threshold: Double,
    val totalConsumption: Double
)
