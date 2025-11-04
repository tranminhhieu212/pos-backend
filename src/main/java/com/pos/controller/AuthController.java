package com.pos.controller;

import com.pos.exception.UserException;
import com.pos.payload.dto.AuthRequest;
import com.pos.payload.dto.UserDto;
import com.pos.payload.response.AuthResponse;
import com.pos.service.AuthService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(
            @RequestBody AuthRequest userDto
    ) throws UserException, Exception {
        return ResponseEntity.ok(
                authService.signup(userDto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody AuthRequest userDto
    ) throws UserException, Exception {
        return ResponseEntity.ok(
                authService.login(userDto)
        );
    }
}
