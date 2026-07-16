package io.github.devcavin.usageservice.model

data class DeviceEnergy(
    val deviceId: Long,
    val energyConsumed: Double,
    val userId: Long? = null
)
