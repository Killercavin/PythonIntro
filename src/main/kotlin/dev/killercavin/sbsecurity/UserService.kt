package dev.killercavin.sbsecurity

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(request: RegisterRequest): UserResponse {
        if (userRepository.existsUserByUsername(request.username)) throw IllegalArgumentException("Username already exists")

        val requestEntity = User(
            username = request.username,
            fullName = request.fullName,
            hashedPassword = passwordEncoder.encode(request.password)
        )

        val newUser = userRepository.save(requestEntity)
        return newUser.toUserResponse()
    }
}