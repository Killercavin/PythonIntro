package io.github.devcavin.wattwise.ingestionservice.mapper;

import io.github.devcavin.wattwise.ingestionservice.dto.EnergyUsageDto;
import io.github.devcavin.wattwise.kafka.event.EnergyUsageEvent;
import org.springframework.stereotype.Component;

@Component
public class EnergyUsageMapper {

    public EnergyUsageEvent toEnergyUsageEvent(EnergyUsageDto dto) {
        return new EnergyUsageEvent(
                dto.deviceId(),
                dto.energyConsumed()
        );
    }
}
