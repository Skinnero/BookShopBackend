package com.codecool.shop.service;

import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.supplier.NewSupplierDto;
import com.codecool.shop.dto.supplier.SupplierDto;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.SupplierRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.Supplier;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.SupplierMapper;
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
    private final ProductRepository productRepository;

    public List<SupplierDto> getSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(supplierMapper::toDto)
                .toList();
    }

    public SupplierDto getSupplierById(UUID id) {
        return supplierMapper.toDto(supplierRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Supplier.class)));
    }

    public void saveNewSupplier(NewSupplierDto newSupplierDto) {
        supplierRepository.save(supplierMapper.dtoToSupplier(newSupplierDto));
    }

    public void updateSupplier(UUID id, NewSupplierDto newSupplierDto) {
        Supplier updatedSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Supplier.class));
        supplierMapper.updateSupplierFromDto(newSupplierDto, updatedSupplier);
        supplierRepository.save(updatedSupplier);
    }

    public void deleteSupplier(UUID id) {
        supplierValidator.validateByEntityId(id);
        supplierRepository.deleteById(id);
    }

    public void assignProductsToSupplier(UUID id, List<ProductIdDto> productIds) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Supplier.class));

        List<Product> productList = productIds
                .stream()
                .map(product -> productRepository.findById(product.id())
                        .orElseThrow(() -> new ObjectNotFoundException(product.id(), Product.class)))
                .toList();

        supplier.assignAllProductsToSupplier(productList);
        supplierRepository.save(supplier);
    }
}
