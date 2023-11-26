package com.codecool.shop.controller;


import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.dto.customer.EditCustomerNameDto;
import com.codecool.shop.dto.customer.EditCustomerPasswordDto;
import com.codecool.shop.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    public final CustomerService customerService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}/name")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> updateCustomerName(@PathVariable UUID id,
                                                      @Valid @RequestBody EditCustomerNameDto editCustomerNameDto) {
        customerService.updateCustomerName(id, editCustomerNameDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> updateCustomerPassword(@PathVariable UUID id,
                                                              @Valid @RequestBody EditCustomerPasswordDto editCustomerPasswordDto) {
        customerService.updateCustomerPassword(id, editCustomerPasswordDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.softDeleteCustomer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
