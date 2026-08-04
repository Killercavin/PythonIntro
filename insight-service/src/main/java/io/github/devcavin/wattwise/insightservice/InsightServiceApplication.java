package io.github.devcavin.wattwise.insightservice;

import io.github.devcavin.wattwise.insightservice.client.UsageClient;
import io.github.devcavin.wattwise.insightservice.config.InsightAppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({UsageClient.class, InsightAppProperties.class})
public class InsightServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsightServiceApplication.class, args);
    }

}
