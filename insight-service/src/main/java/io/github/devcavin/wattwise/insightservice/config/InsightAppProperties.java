package io.github.devcavin.wattwise.insightservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "insight-app")
public record InsightAppProperties(
        String usageUrl
) { }
