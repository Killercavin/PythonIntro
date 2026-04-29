package io.github.devcavin.wattwise.userservice.controller;

import io.github.devcavin.wattwise.userservice.dto.UserDto;
import io.github.devcavin.wattwise.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }
}
