package io.github.devcavin.wattwise.kafka.event;

import java.time.Instant;

public record EnergyUsageEvent(
        Long deviceId,
        double energyConsumed,
        Instant createdAt
) {
    public EnergyUsageEvent(Long deviceId, double energyConsumed) {
        this(deviceId, energyConsumed, Instant.now());
    }
}
