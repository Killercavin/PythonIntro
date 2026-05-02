package io.github.devcavin.wattwise.userservice.dto

import java.time.Instant

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: Instant = Instant.now(),
)