package io.github.devcavin.wattwise.ingestionservice.simulation;

import io.github.devcavin.wattwise.ingestionservice.dto.EnergyUsageDto;
import jakarta.annotation.PreDestroy;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class ParallelDataSimulator implements CommandLineRunner {
    private final ExecutorService executorService;
    private final RestTemplate restTemplate;
    private final Random random;

    @Value("${simulation.parallel-threads}")
    private int parallelThreads;

    @Value("${simulation.request-per-interval}")
    private int requestPerInterval;

    public ParallelDataSimulator() {
        this.executorService = Executors.newCachedThreadPool();
        this.restTemplate = new RestTemplate();
        this.random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("ParallelDataSimulator started");
        ((ThreadPoolExecutor) executorService).setCorePoolSize(parallelThreads);
    }

    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendData() {
        int batchSize = requestPerInterval / parallelThreads;
        int remainder = requestPerInterval % parallelThreads;

        for  (int i = 0; i < parallelThreads; i++) {
            int requestPerThread = batchSize + (i < remainder ? 1 : 0);
            executorService.submit(() -> {
                for  (int j = 0; j < requestPerThread; j++) {
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
            });
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        log.info("ParallelDataSimulator shutdown");
    }
}
