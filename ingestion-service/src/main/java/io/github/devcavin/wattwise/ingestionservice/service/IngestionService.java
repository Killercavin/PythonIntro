package io.github.devcavin.wattwise.ingestionservice.service;

import io.github.devcavin.wattwise.ingestionservice.dto.EnergyUsageDto;
import io.github.devcavin.wattwise.ingestionservice.mapper.EnergyUsageMapper;
import io.github.devcavin.wattwise.kafka.event.EnergyUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IngestionService {
    private final KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate;
    private final EnergyUsageMapper energyUsageMapper;

    public IngestionService(KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate, EnergyUsageMapper energyUsageMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.energyUsageMapper = energyUsageMapper;
    }

    public void ingestEnergyUsage(EnergyUsageDto  dto) {
        EnergyUsageEvent event = energyUsageMapper.toEnergyUsageEvent(dto);

        kafkaTemplate.send("wattwise-energy-usage", event);
        log.info("Ingested energy usage event: {}", event);
    }
}
