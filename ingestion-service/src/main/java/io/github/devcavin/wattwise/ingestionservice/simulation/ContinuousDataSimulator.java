package io.github.devcavin.wattwise.ingestionservice.simulation;

import io.github.devcavin.wattwise.ingestionservice.dto.EnergyUsageDto;
import io.github.devcavin.wattwise.ingestionservice.mapper.EnergyUsageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
@Slf4j
public class ContinuousDataSimulator implements CommandLineRunner {
    private final RestTemplate restTemplate;
    private final EnergyUsageMapper  energyUsageMapper;
    private final Random random;

    @Value("${simulation.request-per-interval}")
    private int requestPerInterval;

    public ContinuousDataSimulator(EnergyUsageMapper energyUsageMapper) {
        this.restTemplate = new RestTemplate();
        this.energyUsageMapper = energyUsageMapper;
        this.random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting DataSimulator...");
    }

    // @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendData() {
        for  (int i = 0; i < requestPerInterval; i++) {
            EnergyUsageDto dto = EnergyUsageDto.builder()
                    .deviceId(random.nextLong(1, 101))
                    .energyConsumed(random.nextDouble(1.0, 1_000_000.0))
                    .build();

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<EnergyUsageDto> request = new HttpEntity<>(dto, headers);
                restTemplate.postForEntity("http://localhost:8083/api/v1/ingestion", request, Void.class);

                log.info("Data sent: {}", dto);
            } catch (Exception e) {
                log.error("Failed sending data {}", e.getMessage());
            }
        }
    }
}
