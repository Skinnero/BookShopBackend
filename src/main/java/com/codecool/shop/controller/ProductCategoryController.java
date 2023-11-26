package com.codecool.shop.controller;


import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.productcategory.NewProductCategoryDto;
import com.codecool.shop.dto.productcategory.ProductCategoryDto;
import com.codecool.shop.service.ProductCategoryService;
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
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController {
    public final ProductCategoryService productCategoryService;

    @GetMapping
    public ResponseEntity<List<ProductCategoryDto>> getAllProductCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(productCategoryService.getProductCategories());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductCategoryDto> getProductCategoryById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(productCategoryService.getProductCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createNewProductCategory(
            @Valid @RequestBody NewProductCategoryDto newProductCategoryDto) {
        productCategoryService.saveNewProductCategory(newProductCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateProductCategory(@PathVariable UUID id,
                                                      @Valid @RequestBody NewProductCategoryDto newProductCategoryDto) {
        productCategoryService.updateProductCategory(id, newProductCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/products")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> assignProductsToProductCategory(@PathVariable UUID id,
                                                                @Valid @RequestBody List<ProductIdDto> productIds) {
        productCategoryService.assignProductsToProductCategory(id, productIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable UUID id) {
        productCategoryService.deleteProductCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
