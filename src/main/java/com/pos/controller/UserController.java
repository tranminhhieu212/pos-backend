package com.pos.controller;

import com.pos.configuration.JwtConstant;
import com.pos.exception.UserException;
import com.pos.modal.User;
import com.pos.payload.dto.UserDto;
import com.pos.payload.mapper.UserMapper;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(
            @RequestHeader(JwtConstant.JWT_HEADER) String token
    ) throws UserException {

        User user = userService.getUserFromJwtToken(token);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(
            @RequestHeader(JwtConstant.JWT_HEADER) String token
    ) throws UserException {

        User user = userService.getCurrentUser();
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @RequestHeader(JwtConstant.JWT_HEADER) String token,
            @PathVariable(name = "id", required = true) Long id
    ) throws UserException {

        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/search")
    public ResponseEntity<UserDto> getUserByEmail(
            @RequestParam(name = "email") String email
    ) throws UserException {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }
}
