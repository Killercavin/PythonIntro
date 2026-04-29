package io.github.devcavin.wattwise.userservice.mapper;

import io.github.devcavin.wattwise.userservice.dto.UserDto;
import io.github.devcavin.wattwise.userservice.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.isAlerting(),
                user.getAlertingThreshold()
        );
    }
}
