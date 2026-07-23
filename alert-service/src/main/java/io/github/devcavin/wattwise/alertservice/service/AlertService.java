package io.github.devcavin.wattwise.alertservice.service;

import io.github.devcavin.wattwise.kafka.event.AlertingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertService {
    @KafkaListener(
            topics = "wattwise-energy-alert",
            groupId = "wattwise-alert-service"
    )
    public void energyUsageAlertEvent(AlertingEvent  alertingEvent) {
        log.info("Received alertingEvent {}", alertingEvent);
    }
}
