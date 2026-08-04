package io.github.devcavin.wattwise.insightservice.dto;

import lombok.Builder;

@Builder
public record DeviceDto(
        Long id,
        String name,
        String type,
        String location,
        double energyConsumed
) { }
