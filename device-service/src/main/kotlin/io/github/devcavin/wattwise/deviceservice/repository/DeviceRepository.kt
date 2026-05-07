package io.github.devcavin.wattwise.deviceservice.repository

import io.github.devcavin.wattwise.deviceservice.entity.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : JpaRepository<Device, Long> {
}