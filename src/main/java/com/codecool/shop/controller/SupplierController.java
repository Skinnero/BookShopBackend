package com.codecool.shop.controller;


import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.supplier.NewSupplierDto;
import com.codecool.shop.dto.supplier.SupplierDto;
import com.codecool.shop.service.SupplierService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    public final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getSuppliers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getSupplierById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createNewSupplier(@Valid @RequestBody NewSupplierDto newSupplierDto) {
        supplierService.saveNewSupplier(newSupplierDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateSupplier(@PathVariable UUID id,
                                               @Valid @RequestBody NewSupplierDto newSupplierDto) {
        supplierService.updateSupplier(id, newSupplierDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/products")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> assignProductsToSupplier(@PathVariable UUID id,
                                                         @Valid @RequestBody List<ProductIdDto> productIds) {
        supplierService.assignProductsToSupplier(id, productIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
