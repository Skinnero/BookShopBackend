package com.codecool.shop.controller;


import com.codecool.shop.dto.ordertransaction.NewOrderTransactionDto;
import com.codecool.shop.dto.ordertransaction.OrderTransactionDto;
import com.codecool.shop.service.OrderTransactionService;
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
@RequestMapping("/api/v1/order-transactions")
public class OrderTransactionController {
    public final OrderTransactionService orderTransactionService;

    @GetMapping(params = "customerId")
    @PreAuthorize("hasRole('ROLE_USER')")
    private ResponseEntity<List<OrderTransactionDto>> getAllOrderTransactionsByCustomerId(
            @RequestParam("customerId") UUID customerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderTransactionService.getOrderTransactionByCustomerId(customerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> createNewOrderTransaction(
            @Valid @RequestBody NewOrderTransactionDto newTransactionDto) {
        orderTransactionService.saveNewOrderTransaction(newTransactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
