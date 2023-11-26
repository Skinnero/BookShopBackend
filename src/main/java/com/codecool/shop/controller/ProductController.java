package com.codecool.shop.controller;


import com.codecool.shop.dto.product.NewProductDto;
import com.codecool.shop.dto.product.ProductDto;
import com.codecool.shop.service.ProductService;
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
@RequestMapping("/api/v1/products")
public class ProductController {
    public final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
    }

    @GetMapping(params = {"supplierId", "productCategoryId"})
    public ResponseEntity<List<ProductDto>> getFilteredProducts(
            @RequestParam(value = "supplierId", required = false) UUID supplierId,
            @RequestParam(value = "productCategoryId", required = false) UUID productCategoryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getFilteredProducts(supplierId, productCategoryId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createNewProduct(@Valid @RequestBody NewProductDto newProductDto) {
        productService.saveNewProduct(newProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID id,
                                              @Valid @RequestBody NewProductDto newProductDto) {
        productService.updateProduct(id, newProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
