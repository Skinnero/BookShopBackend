package com.codecool.shop.service.validator;

import com.codecool.shop.repository.AddressRepository;
import com.codecool.shop.repository.entity.Address;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AddressValidator implements Validator {
    private final AddressRepository addressRepository;

    @Override
    public void validateByEntityId(UUID id) {
        addressRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Address.class));
    }
}
