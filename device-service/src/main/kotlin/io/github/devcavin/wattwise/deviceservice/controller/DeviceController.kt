package io.github.devcavin.wattwise.deviceservice.controller

import io.github.devcavin.wattwise.deviceservice.dto.DeviceRequest
import io.github.devcavin.wattwise.deviceservice.dto.DeviceResponse
import io.github.devcavin.wattwise.deviceservice.service.DeviceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/devices")
class DeviceController(private val deviceService: DeviceService) {
    @PostMapping
    fun add(@RequestBody request: DeviceRequest): ResponseEntity<DeviceResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(deviceService.create(request))
    }

    @GetMapping
    fun allDevices(): ResponseEntity<List<DeviceResponse>> {
        return ResponseEntity.ok(deviceService.allDevices())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<DeviceResponse> {
        return ResponseEntity.ok(
            deviceService.deviceById(id)
        )
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        deviceService.delete(id)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: DeviceRequest): ResponseEntity<DeviceResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(deviceService.update(id, request))
    }

    @GetMapping("/user/{userId}")
    fun getDevicesByUser(@PathVariable userId: Long): ResponseEntity<List<DeviceResponse>> {
        return ResponseEntity.ok(deviceService.getDevicesByUser(userId))
    }
}