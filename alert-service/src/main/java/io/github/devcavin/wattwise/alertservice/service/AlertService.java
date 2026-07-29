package io.github.devcavin.wattwise.alertservice.service;

import io.github.devcavin.wattwise.kafka.event.AlertingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertService {
    private final EmailService emailService;

    public AlertService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "wattwise-energy-alert",
            groupId = "wattwise-alert-service"
    )
    public void energyUsageAlertEvent(AlertingEvent  alertingEvent) {
        log.info("Received alertingEvent {}", alertingEvent);

        final String subject = "Energy usage alert for user " + alertingEvent.getUserId();
        final String message = "Alert: " + alertingEvent.getMessage() + "\nThreshold: "  + alertingEvent.getThreshold() + "\nEnergyConsumed: " + alertingEvent.getEnergyConsumed();

        emailService.sendEmail(alertingEvent.getEmail(), subject, message, alertingEvent.getUserId());
    }
}
