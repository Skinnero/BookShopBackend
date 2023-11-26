package com.codecool.shop.service;

import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.productcategory.NewProductCategoryDto;
import com.codecool.shop.dto.productcategory.ProductCategoryDto;
import com.codecool.shop.repository.ProductCategoryRepository;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.ProductCategory;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.ProductCategoryMapper;
import com.codecool.shop.service.validator.ProductCategoryValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductCategoryValidator productCategoryValidator;
    private final ProductRepository productRepository;

    public List<ProductCategoryDto> getProductCategories() {
        return productCategoryRepository.findAll()
                .stream()
                .map(productCategoryMapper::toDto)
                .toList();
    }

    public ProductCategoryDto getProductCategoryById(UUID id) {
        return productCategoryMapper.toDto(productCategoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ProductCategory.class)));
    }

    public void saveNewProductCategory(NewProductCategoryDto newProductCategoryDto) {
        productCategoryRepository.save(productCategoryMapper.dtoToProductCategory(newProductCategoryDto));
    }

    public void updateProductCategory(UUID id, NewProductCategoryDto newProductCategoryDto) {
        ProductCategory updatedProductCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ProductCategory.class));
        productCategoryMapper.updateProductCategoryFromDto(newProductCategoryDto, updatedProductCategory);
        productCategoryRepository.save(updatedProductCategory);
    }

    public void deleteProductCategory(UUID id) {
        productCategoryValidator.validateByEntityId(id);
        productCategoryRepository.deleteById(id);
    }

    public void assignProductsToProductCategory(UUID id, List<ProductIdDto> productIds) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ProductCategory.class));

        List<Product> productList = productIds
                .stream()
                .map(productId -> productRepository.findById(productId.id())
                        .orElseThrow(() -> new ObjectNotFoundException(productId.id(), Product.class)))
                .toList();

        productCategory.assignAllProductsToProductCategory(productList);
        productCategoryRepository.save(productCategory);
    }
}
