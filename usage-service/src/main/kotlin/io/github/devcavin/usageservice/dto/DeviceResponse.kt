package io.github.devcavin.usageservice.dto

data class DeviceResponse(
    val id: Long,
    val name: String,
    val location: String,
    val type: String,
    val userId: Long
)
