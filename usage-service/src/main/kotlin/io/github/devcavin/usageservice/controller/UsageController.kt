package io.github.devcavin.usageservice.controller

import io.github.devcavin.usageservice.dto.UsageDto
import io.github.devcavin.usageservice.service.UsageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/usages")
class UsageController(private val usageService: UsageService) {
    @GetMapping("/{usageId}")
    fun getUserDeviceUsage(@PathVariable usageId: Long, @RequestParam(defaultValue = "3") days: Int): ResponseEntity<UsageDto> {
        val usage = usageService.getXDaysUsageForUser(usageId, days)
        return ResponseEntity.ok(usage)
    }
}