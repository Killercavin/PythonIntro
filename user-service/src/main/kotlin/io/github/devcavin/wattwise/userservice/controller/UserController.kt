package io.github.devcavin.wattwise.userservice.controller

import io.github.devcavin.wattwise.userservice.dto.UserRequest
import io.github.devcavin.wattwise.userservice.dto.UserResponse
import io.github.devcavin.wattwise.userservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.createUser(request))
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.findAllUsers())
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.findUserById(id))
    }

    @GetMapping("/email")
    fun getUserByEmail(@RequestParam email: String): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.findUserByEmail(email))
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UserRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
        .body(userService.updateUser(id, request))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
}