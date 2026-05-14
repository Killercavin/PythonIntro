package io.github.devcavin.wattwise.deviceservice.dto

import io.github.devcavin.wattwise.deviceservice.entity.Device
import io.github.devcavin.wattwise.deviceservice.enums.DeviceType

data class DeviceResponse(
    val id: Long,
    val name: String,
    val location: String,
    val type: DeviceType,
    val userId: Long
)

fun Device.toDeviceResponse() = DeviceResponse(
    id = this.id!!,
    name = this.name,
    location = this.location,
    type = this.type,
    userId = this.userId
)
