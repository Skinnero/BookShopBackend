package com.codecool.shop.dto.customer;

import com.codecool.shop.controller.requestvalidator.UnoccupiedEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewCustomerDto(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @Email(message = "Email is invalid")
        @NotBlank(message = "Email cannot be empty")
        @UnoccupiedEmail
        String email,
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
