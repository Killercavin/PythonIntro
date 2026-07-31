package io.github.devcavin.usageservice.dto

data class UsageDto(
    val userId: Long,
    val devices: List<DeviceResponse>
)
