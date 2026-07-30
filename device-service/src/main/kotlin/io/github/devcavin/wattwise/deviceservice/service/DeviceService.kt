package io.github.devcavin.wattwise.deviceservice.service

import io.github.devcavin.wattwise.deviceservice.ResourceNotFoundException
import io.github.devcavin.wattwise.deviceservice.dto.DeviceRequest
import io.github.devcavin.wattwise.deviceservice.dto.DeviceResponse
import io.github.devcavin.wattwise.deviceservice.dto.toDeviceEntity
import io.github.devcavin.wattwise.deviceservice.dto.toDeviceResponse
import io.github.devcavin.wattwise.deviceservice.repository.DeviceRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DeviceService(private val deviceRepository: DeviceRepository) {
    fun allDevices(): List<DeviceResponse> {
        return deviceRepository.findAll().map { it.toDeviceResponse() }
    }

    fun deviceById(id: Long): DeviceResponse {
        return deviceRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Device not found") }
            .toDeviceResponse()
    }

    fun create(request: DeviceRequest): DeviceResponse {
        return deviceRepository.save(request.toDeviceEntity()).toDeviceResponse()
    }

    @Transactional
    fun update(id: Long, request: DeviceRequest): DeviceResponse {
        val device = deviceRepository.findById(id).orElseThrow { ResourceNotFoundException("Device not found") }
        device.update(request)

        return device.toDeviceResponse()
    }

    fun delete(id: Long) {
        if (!deviceRepository.existsById(id)) throw ResourceNotFoundException("Device not found")
        deviceRepository.deleteById(id)
    }

    fun getAllDevicesByUser(userId: Long): List<DeviceResponse> {
        return deviceRepository.findByUserId(userId)
            .map { it.toDeviceResponse() }
    }
}