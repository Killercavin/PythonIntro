package io.github.devcavin.usageservice.client

import io.github.devcavin.usageservice.config.DeviceProperties
import io.github.devcavin.usageservice.dto.DeviceResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.util.UriComponentsBuilder

@Component
class DeviceClient(
    private val deviceProperties: DeviceProperties,
    private val restTemplate: RestTemplate
) {
    fun getDeviceById(deviceId: Long): DeviceResponse {
        val url = UriComponentsBuilder
            .fromUriString(deviceProperties.deviceUrl)
            .path("/{deviceId}")
            .buildAndExpand(deviceId)
        .toUriString()

        val response: ResponseEntity<DeviceResponse> = restTemplate.getForEntity(url)

        return response.body ?: throw RestClientException("Device not found")
    }
}