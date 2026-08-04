package io.github.devcavin.wattwise.insightservice.controller;

import io.github.devcavin.wattwise.insightservice.dto.InsightDto;
import io.github.devcavin.wattwise.insightservice.service.InsightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/insights")
public class InsightController {
    private final InsightService insightService;


    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping("/saving-tips/{userId}")
    public ResponseEntity<InsightDto> getSavingTips(@PathVariable Long userId) {
        final InsightDto insight = insightService.getSavingsTips(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(insight);
    }

    @GetMapping("/overview/{userId}")
    public ResponseEntity<InsightDto> getOverview(@PathVariable Long userId) {
        final InsightDto overview = insightService.getOverview(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(overview);
    }
}
