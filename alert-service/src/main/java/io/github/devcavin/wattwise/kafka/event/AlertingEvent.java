package io.github.devcavin.wattwise.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AlertingEvent {
    private Long userId;
    private String message;
    private double threshold;
    private double energyConsumed;
    private String email;
}
