package io.github.devcavin.wattwise.insightservice.client;

import io.github.devcavin.wattwise.insightservice.config.InsightAppProperties;
import io.github.devcavin.wattwise.insightservice.dto.UsageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UsageClient {
    private final RestTemplate restTemplate;
    private final InsightAppProperties properties;

    public UsageClient(InsightAppProperties properties) {
        this.restTemplate = new RestTemplate();
        this.properties = properties;
    }

    public UsageDto getXDaysUsageForUser(Long userId, int days) {
        String url = UriComponentsBuilder
                .fromUriString(properties.usageUrl())
                .path("/{userId}")
                .queryParam("days", days)
                .buildAndExpand(userId)
                .toUriString();

        ResponseEntity<UsageDto> response = restTemplate.getForEntity(url, UsageDto.class);

        return response.getBody();
    }
}
