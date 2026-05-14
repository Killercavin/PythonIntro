package io.github.devcavin.wattwise.deviceservice.entity

import io.github.devcavin.wattwise.deviceservice.dto.DeviceRequest
import io.github.devcavin.wattwise.deviceservice.dto.DeviceResponse
import io.github.devcavin.wattwise.deviceservice.enums.DeviceType
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "devices")
class Device(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,

    @Enumerated(EnumType.STRING)
    var type: DeviceType,

    var location: String,

    var userId: Long
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as Device
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: Hibernate.getClass(this).hashCode()
    }

    override fun toString(): String {
        return "Device(id=$id, name='$name', type=$type, location='$location', userId=$userId)"
    }

    fun update(request: DeviceRequest) {
        name = request.name
        type = request.type
        location = request.location
        userId = request.userId
    }
}