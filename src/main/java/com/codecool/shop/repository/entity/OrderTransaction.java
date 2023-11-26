package com.codecool.shop.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime submissionTime;
    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private Basket basket;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;
}
