package io.github.devcavin.wattwise.ingestionservice.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record EnergyUsageDto(
        Long deviceId,
        double energyConsumed,
        Instant createdAt
) { }
