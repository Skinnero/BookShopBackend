package com.codecool.shop.controller;


import com.codecool.shop.dto.basket.BasketDto;
import com.codecool.shop.dto.basket.EditBasketDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.service.BasketService;
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
@RequestMapping("/api/v1/baskets")
public class BasketController {
    public final BasketService basketService;

    @GetMapping(params = "customerId")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BasketDto> getBasketByCustomerId(@RequestParam("customerId") UUID customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.getBasketByCustomerId(customerId));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BasketDto> getBasketById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.getBasketById(id));
    }

    @GetMapping(value = "/detailed", params = "customerId")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<BasketDto>> getAllBasketsWithProducts(@RequestParam("customerId") UUID customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.getAllBasketsWithProductsByCustomerId(customerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> createNewBasket(@Valid @RequestBody NewBasketDto newBasketDto) {
        basketService.saveNewBasket(newBasketDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> assignProductsToBasket(@PathVariable UUID id,
                                                       @Valid @RequestBody EditBasketDto editBasketDto) {
        basketService.updateBasket(id, editBasketDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> deleteBasket(@PathVariable UUID id) {
        basketService.deleteBasket(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
