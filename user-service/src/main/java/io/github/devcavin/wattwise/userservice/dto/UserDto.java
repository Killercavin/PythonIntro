package io.github.devcavin.wattwise.userservice.dto;

public record UserDto(
        Long id,
        String fullName,
        String email,
        boolean alerting,
        double alertingThreshold
) { }