package io.github.devcavin.wattwise.deviceservice.dto

import io.github.devcavin.wattwise.deviceservice.entity.Device
import io.github.devcavin.wattwise.deviceservice.enums.DeviceType

data class DeviceRequest(
    var name: String,
    var type: DeviceType,
    var location: String,
    var userId: Long
)

fun DeviceRequest.toDeviceEntity(): Device {
    return Device(
        name = this.name,
        type = this.type,
        location = this.location,
        userId = this.userId
    )
}
