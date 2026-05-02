package io.github.devcavin.wattwise.userservice.repository

import io.github.devcavin.wattwise.userservice.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    override fun findById(id: Long): Optional<User>
    fun findByEmail(email: String): Optional<User>
}