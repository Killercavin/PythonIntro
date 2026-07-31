package io.github.devcavin.usageservice.client

import io.github.devcavin.usageservice.config.UsageAppProperties
import io.github.devcavin.usageservice.dto.DeviceResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.util.UriComponentsBuilder

@Component
class DeviceClient(
    private val restTemplate: RestTemplate,
    private val properties: UsageAppProperties
) {
    fun getDeviceById(deviceId: Long): DeviceResponse {
        val url = UriComponentsBuilder
            .fromUriString(properties.deviceUrl)
            .path("/{deviceId}")
            .buildAndExpand(deviceId)
            .toUriString()

        val response: ResponseEntity<DeviceResponse> = restTemplate.getForEntity(url)

        return response.body ?: throw RestClientException("Device not found")
    }

    fun getAllDevicesForUser(userId: Long): List<DeviceResponse> {
        val url = UriComponentsBuilder
            .fromUriString(properties.deviceUrl)
            .path("/user/$userId")
            .buildAndExpand(userId)
            .toUriString()

        val response: ResponseEntity<List<DeviceResponse>> = restTemplate.getForEntity(url, listOf(DeviceResponse::class.java))

        return response.body ?: throw RestClientException("Device not found")
    }
}