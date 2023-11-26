package com.codecool.shop.service;

import com.codecool.shop.dto.product.NewProductDto;
import com.codecool.shop.dto.product.ProductDto;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.ProductMapper;
import com.codecool.shop.service.validator.ProductValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDto getProductById(UUID id) {
        return productMapper.toDto(productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Product.class)));
    }

    public void saveNewProduct(NewProductDto newProductDto) {
        productRepository.save(productMapper.dtoToProduct(newProductDto));
    }

    public void updateProduct(UUID id, NewProductDto newProductDto) {
        Product updatedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Product.class));
        productMapper.updateProductFromDto(newProductDto, updatedProduct);
        productRepository.save(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        productValidator.validateByEntityId(id);
        productRepository.deleteById(id);
    }

    public List<ProductDto> getFilteredProducts(UUID supplierId, UUID productCategoryId) {
        return productRepository.findAllBySupplierIdAndProductCategoryId(supplierId, productCategoryId)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
}
