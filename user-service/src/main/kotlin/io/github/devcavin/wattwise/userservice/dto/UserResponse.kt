package io.github.devcavin.wattwise.userservice.dto

import io.github.devcavin.wattwise.userservice.entity.User

data class UserResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val address: String,
    val alerting: Boolean,
    val alertingThreshold: Double
)

fun User.toUserResponse(): UserResponse {
    return UserResponse(
        id = this.id!!,
        fullName = this.fullName,
        email = this.email,
        address = this.address,
        alerting = this.alerting,
        alertingThreshold = this.alertingThreshold
    )
}
