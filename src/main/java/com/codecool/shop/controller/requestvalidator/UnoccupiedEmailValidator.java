package com.codecool.shop.controller.requestvalidator;

import com.codecool.shop.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UnoccupiedEmailValidator implements ConstraintValidator<UnoccupiedEmail, String> {
    private final CustomerRepository customerRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return customerRepository.findByEmail(value).isEmpty();
    }
}
