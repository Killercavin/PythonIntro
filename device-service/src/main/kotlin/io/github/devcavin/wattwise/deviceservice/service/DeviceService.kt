package io.github.devcavin.wattwise.deviceservice.service

import io.github.devcavin.wattwise.deviceservice.repository.DeviceRepository
import org.springframework.stereotype.Service

@Service
class DeviceService(private val deviceRepository: DeviceRepository) {
}