package io.github.devcavin.wattwise.userservice.service

import io.github.devcavin.wattwise.userservice.ResourceNotFoundException
import io.github.devcavin.wattwise.userservice.dto.UserRequest
import io.github.devcavin.wattwise.userservice.dto.UserResponse
import io.github.devcavin.wattwise.userservice.dto.toUserEntity
import io.github.devcavin.wattwise.userservice.dto.toUserResponse
import io.github.devcavin.wattwise.userservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(request: UserRequest): UserResponse {
        return userRepository.save(request.toUserEntity()).toUserResponse()
    }

    fun findAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { it.toUserResponse() }
    }

    fun findUserByEmail(email: String): UserResponse {
        return userRepository.findByEmail(email)
            .orElseThrow { ResourceNotFoundException("User not found") }
            .toUserResponse()
    }

    fun findUserById(id: Long): UserResponse {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found") }
            .toUserResponse()
    }

    @Transactional
    fun updateUser(id: Long, request: UserRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found") }

        user.update(request)

        return user.toUserResponse()
    }

    fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("User not found")
        }
        userRepository.deleteById(id)
    }
}