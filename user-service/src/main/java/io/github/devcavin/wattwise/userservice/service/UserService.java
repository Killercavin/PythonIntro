package io.github.devcavin.wattwise.userservice.service;

import io.github.devcavin.wattwise.userservice.dto.UserDto;
import io.github.devcavin.wattwise.userservice.entity.User;
import io.github.devcavin.wattwise.userservice.mapper.UserMapper;
import io.github.devcavin.wattwise.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto request) {
        final User newUser = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .alerting(request.alerting())
                .alertingThreshold(request.alertingThreshold())
                .build();

        User savedUser = userRepository.save(newUser);
        
        log.info("User created {}", savedUser);
        return UserMapper.toDto(savedUser);
    }
}
