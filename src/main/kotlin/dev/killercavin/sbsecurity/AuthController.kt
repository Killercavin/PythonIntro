package dev.killercavin.sbsecurity

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {
    // Auth register endpoint
    @PostMapping("/signup")
    fun signup(@Validated @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request))
    }

    // Auth login
    @PostMapping("/login")
    fun login(@Validated @RequestBody request: LoginRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request))
    }
}