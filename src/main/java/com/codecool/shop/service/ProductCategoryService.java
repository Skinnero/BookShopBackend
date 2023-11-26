package com.codecool.shop.service;

import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.productcategory.NewProductCategoryDto;
import com.codecool.shop.dto.productcategory.ProductCategoryDto;
import com.codecool.shop.repository.ProductCategoryRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.ProductCategory;
import com.codecool.shop.service.mapper.ProductCategoryMapper;
import com.codecool.shop.service.validator.ProductCategoryValidator;
import com.codecool.shop.service.validator.ProductValidator;
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
    private final ProductValidator productValidator;

    public List<ProductCategoryDto> getProductCategories() {
        return productCategoryRepository.findAll()
                .stream()
                .map(productCategoryMapper::toDto)
                .toList();
    }

    public ProductCategoryDto getProductCategoryById(UUID id) {
        return productCategoryMapper.toDto(productCategoryValidator.validateByEntityId(id));
    }

    public void saveNewProductCategory(NewProductCategoryDto newProductCategoryDto) {
        productCategoryRepository.save(productCategoryMapper.dtoToProductCategory(newProductCategoryDto));
    }

    public void updateProductCategory(UUID id, NewProductCategoryDto newProductCategoryDto) {
        ProductCategory updatedProductCategory = productCategoryValidator.validateByEntityId(id);
        productCategoryMapper.updateProductCategoryFromDto(newProductCategoryDto, updatedProductCategory);
        productCategoryRepository.save(updatedProductCategory);
    }

    public void deleteProductCategory(UUID id) {
        productCategoryRepository.delete(productCategoryValidator.validateByEntityId(id));
    }

    public void assignProductsToProductCategory(UUID id, List<ProductIdDto> productIds) {
        ProductCategory productCategory = productCategoryValidator.validateByEntityId(id);

        List<Product> productList = productIds
                .stream()
                .map(productId -> productValidator.validateByEntityId(productId.id()))
                .toList();

        productCategory.assignAllProductsToProductCategory(productList);
        productCategoryRepository.save(productCategory);
    }
}
