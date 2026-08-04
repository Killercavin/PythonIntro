package io.github.devcavin.wattwise.insightservice.dto;

import lombok.Builder;

@Builder
public record InsightDto (
    Long userId,
    String tips,
    double energyUsage
){ }
