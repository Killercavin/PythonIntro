package io.github.devcavin.wattwise.userservice.entity

import io.github.devcavin.wattwise.userservice.dto.UserRequest
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var fullName: String,
    var email: String,
    var address: String,
    var alerting: Boolean = false,
    var alertingThreshold: Double = 0.0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as User
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: Hibernate.getClass(this).hashCode()
    }



    fun update(request: UserRequest) {
        fullName = request.fullName
        email = request.email
        address = request.address
        alerting = request.alerting
        alertingThreshold = request.alertingThreshold
    }

    override fun toString(): String {
        return "User(id=$id, fullName='$fullName', email='$email', address='$address', alerting=$alerting, alertingThreshold=$alertingThreshold)"
    }
}