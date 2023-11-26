package com.codecool.shop.controller;

import com.codecool.shop.dto.address.AddressDto;
import com.codecool.shop.dto.address.EditAddressDto;
import com.codecool.shop.dto.address.NewAddressDto;
import com.codecool.shop.service.AddressService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {
    private final AddressService addressService;

    @GetMapping(params = "customerId")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AddressDto> getAddressByCustomerId(@RequestParam("customerId") UUID customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.getAddressByCustomerId(customerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> createNewAddress(@Valid @RequestBody NewAddressDto newAddressDto) {
        addressService.saveNewAddress(newAddressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> updateAddress(@PathVariable UUID id,
                                                    @Valid @RequestBody EditAddressDto editAddressDto) {
        addressService.updateAddress(id, editAddressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
