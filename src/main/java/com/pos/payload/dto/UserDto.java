package com.pos.payload.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private Long id;

    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    private UserRole role;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    private String password;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate lastLoginAt;
}
