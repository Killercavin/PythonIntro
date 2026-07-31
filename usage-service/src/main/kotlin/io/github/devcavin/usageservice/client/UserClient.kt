package io.github.devcavin.usageservice.client

import io.github.devcavin.usageservice.config.UsageAppProperties
import io.github.devcavin.usageservice.dto.UserResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.util.UriComponentsBuilder

@Component
class UserClient(
    private val restTemplate: RestTemplate,
    private val properties: UsageAppProperties
) {
    fun getUserById(userId: Long): UserResponse {
        val url = UriComponentsBuilder.fromUriString(properties.userUrl)
            .path("/{userId}")
        .buildAndExpand(userId)
            .toUriString()

        val response = restTemplate.getForEntity<UserResponse>(url)

        return response.body ?: throw RestClientException("User not found")
    }
}
