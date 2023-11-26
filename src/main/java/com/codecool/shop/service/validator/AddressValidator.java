package com.codecool.shop.service.validator;

import com.codecool.shop.repository.AddressRepository;
import com.codecool.shop.repository.entity.Address;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AddressValidator implements Validator<Address> {
    private final AddressRepository addressRepository;

    @Override
    public Address validateByEntityId(UUID id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Address.class));
    }
}
