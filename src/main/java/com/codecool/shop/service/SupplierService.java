package com.codecool.shop.service;

import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.supplier.NewSupplierDto;
import com.codecool.shop.dto.supplier.SupplierDto;
import com.codecool.shop.repository.SupplierRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.Supplier;
import com.codecool.shop.service.mapper.SupplierMapper;
import com.codecool.shop.service.validator.ProductValidator;
import com.codecool.shop.service.validator.SupplierValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierValidator supplierValidator;
    private final ProductValidator productValidator;

    public List<SupplierDto> getSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toDto)
                .toList();
    }

    public SupplierDto getSupplierById(UUID id) {
        return supplierMapper.toDto(supplierValidator.validateByEntityId(id));
    }

    public void saveNewSupplier(NewSupplierDto newSupplierDto) {
        supplierRepository.save(supplierMapper.dtoToSupplier(newSupplierDto));
    }

    public void updateSupplier(UUID id, NewSupplierDto newSupplierDto) {
        Supplier updatedSupplier = supplierValidator.validateByEntityId(id);
        supplierMapper.updateSupplierFromDto(newSupplierDto, updatedSupplier);
        supplierRepository.save(updatedSupplier);
    }

    public void deleteSupplier(UUID id) {
        supplierRepository.delete(supplierValidator.validateByEntityId(id));
    }

    public void assignProductsToSupplier(UUID id, List<ProductIdDto> productIds) {
        Supplier supplier = supplierValidator.validateByEntityId(id);

        List<Product> productList = productIds
                .stream()
                .map(product -> productValidator.validateByEntityId(product.id()))
                .toList();

        supplier.assignAllProductsToSupplier(productList);
        supplierRepository.save(supplier);
    }
}
