package com.codecool.shop.service.validator;

import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.exception.EmailNotFoundException;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CustomerValidator implements Validator {
    private final CustomerRepository customerRepository;

    @Override
    public void validateByEntityId(UUID id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Customer.class));
    }

    public void validateByEmail(String email) {
        customerRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));
    }
}
