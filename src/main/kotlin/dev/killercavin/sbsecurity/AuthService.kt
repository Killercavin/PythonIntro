package dev.killercavin.sbsecurity

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
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

    fun login(loginRequest: LoginRequest): UserResponse {
        val authToken = UsernamePasswordAuthenticationToken(
            loginRequest.username,
            loginRequest.password
        )

        val authentication = authenticationManager.authenticate(authToken)
        val userInformation = authentication.principal as CustomUserDetails

        return userInformation.user.toUserResponse()
    }
}