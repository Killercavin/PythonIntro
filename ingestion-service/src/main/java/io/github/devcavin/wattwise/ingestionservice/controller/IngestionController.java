package io.github.devcavin.wattwise.ingestionservice.controller;

import io.github.devcavin.wattwise.ingestionservice.dto.EnergyUsageDto;
import io.github.devcavin.wattwise.ingestionservice.service.IngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {
    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void ingestData(@RequestBody EnergyUsageDto request) {
        ingestionService.ingestEnergyUsage(request);
    }
}