package dev.killercavin.sbsecurity

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun loadByUsername(username: String): UserResponse?
    fun existsUserByUsername(username: String): Boolean
}