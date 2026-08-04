package io.github.devcavin.wattwise.insightservice.service;

import io.github.devcavin.wattwise.insightservice.client.UsageClient;
import io.github.devcavin.wattwise.insightservice.dto.DeviceDto;
import io.github.devcavin.wattwise.insightservice.dto.InsightDto;
import io.github.devcavin.wattwise.insightservice.dto.UsageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class InsightService {
    private final UsageClient  usageClient;
    private final OllamaChatModel ollamaChatModel;

    public InsightService(UsageClient usageClient, OllamaChatModel ollamaChatModel) {
        this.usageClient = usageClient;
        this.ollamaChatModel = ollamaChatModel;
    }


    public InsightDto getSavingsTips(Long userId) {
        final UsageDto usageData = usageClient.getXDaysUsageForUser(userId, 3);

        double totalUsage = usageData
                .devices().stream()
                .mapToDouble(DeviceDto::energyConsumed)
                .sum();

        log.info("Ollama savings tips insight for user id: {} with total usage {}", userId, totalUsage);

        String prompt = "This is my total consumption over the past 3 days. How can I reduce my energy consumption? How does it compare to average households? Total energy used: \n" + totalUsage;

        ChatResponse response = ollamaChatModel.call(
                Prompt.builder()
                        .content(prompt)
                        .build());

        return InsightDto.builder()
                .userId(userId)
                .tips(Objects.requireNonNull(response.getResult()).getOutput().getText())
                .energyUsage(totalUsage)
                .build();
    }

    public InsightDto getOverview(Long userId) {
        final UsageDto usageData = usageClient.getXDaysUsageForUser(userId, 3);

        double totalUsage = usageData
                .devices().stream()
                .mapToDouble(DeviceDto::energyConsumed)
                .sum();

        log.info("Ollama insight overview for user id: {} with total usage {}", userId, totalUsage);

        String prompt = "Analyse the following energy usage data and provide a concise overview with actionable insights. This data is the aggregate data for the past 3 days. Usage Data: \n" + usageData.devices();

        ChatResponse response = ollamaChatModel.call(
                Prompt.builder()
                        .content(prompt)
                        .build());

        return InsightDto.builder()
                .userId(userId)
                .tips(Objects.requireNonNull(response.getResult()).getOutput().getText())
                .energyUsage(totalUsage)
                .build();
    }
}
