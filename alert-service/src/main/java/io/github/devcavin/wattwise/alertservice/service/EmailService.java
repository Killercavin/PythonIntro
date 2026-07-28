package io.github.devcavin.wattwise.alertservice.service;

import io.github.devcavin.wattwise.alertservice.entity.Alert;
import io.github.devcavin.wattwise.alertservice.repository.AlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final AlertRepository alertRepository;


    public EmailService(JavaMailSender javaMailSender, AlertRepository alertRepository) {
        this.javaMailSender = javaMailSender;
        this.alertRepository = alertRepository;
    }

    public void sendEmail(String to, String subject, String body, Long userId) {
        log.info("Sending email to {}, subject {}", to, subject);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom("noreply@wattwise.app");
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        try {
            javaMailSender.send(simpleMailMessage);

            final Alert alertSent = Alert.builder()
                    .sent(true)
                    .createdAt(LocalDateTime.now())
                    .userId(userId)
                    .build();

            alertRepository.saveAndFlush(alertSent);
            log.info("Email alert sent to {}", to);

        } catch (Exception e) {
            final Alert failedAlert = Alert.builder()
                    .sent(false)
                    .createdAt(LocalDateTime.now())
                    .userId(userId)
                    .build();

            alertRepository.saveAndFlush(failedAlert);
            log.error("Failed to send email alert to {}",  to, e);
        }
    }
}
