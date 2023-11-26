package com.codecool.shop.service.validator;

import com.codecool.shop.repository.SupplierRepository;
import com.codecool.shop.repository.entity.Supplier;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class SupplierValidator implements Validator {
    private final SupplierRepository supplierRepository;

    @Override
    public void validateByEntityId(UUID id) {
        supplierRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Supplier.class));
    }
}
