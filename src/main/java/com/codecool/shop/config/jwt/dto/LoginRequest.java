package com.codecool.shop.config.jwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email cannot be empty")
        @Email
        String email,
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
