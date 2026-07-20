package io.github.devcavin.usageservice.dto

data class UserResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val address: String,
    val alerting: Boolean,
    val alertingThreshold: Double
)
