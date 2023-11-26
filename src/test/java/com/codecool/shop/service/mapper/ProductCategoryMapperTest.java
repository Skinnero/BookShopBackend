package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.productcategory.NewProductCategoryDto;
import com.codecool.shop.dto.productcategory.ProductCategoryDto;
import com.codecool.shop.repository.entity.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductCategoryMapperTest {
    ProductCategoryMapper mapper = Mappers.getMapper(ProductCategoryMapper.class);

    private UUID productCategoryId;
    private ProductCategory productCategory;
    private NewProductCategoryDto newProductCategoryDto;

    @BeforeEach
    void setUp() {
        productCategoryId = UUID.randomUUID();

        productCategory = new ProductCategory(
                productCategoryId,
                "Name",
                "Department"
        );

        newProductCategoryDto = new NewProductCategoryDto(
                "Name2",
                "Department2"
        );
    }

    @Test
    void testToDto_ShouldReturnProductCategoryDto_WhenCalled() {
        // when
        ProductCategoryDto expectedProductCategoryDto = mapper.toDto(productCategory);

        // then
        assertThat(expectedProductCategoryDto.id()).isEqualTo(productCategory.getId());
        assertThat(expectedProductCategoryDto.name()).isEqualTo(productCategory.getName());
        assertThat(expectedProductCategoryDto.department()).isEqualTo(productCategory.getDepartment());
    }

    @Test
    void testDtoToProductCategory_ShouldReturnProductCategory_WhenCalled() {
        // when
        ProductCategory expectedProductCategory = mapper.dtoToProductCategory(newProductCategoryDto);

        // then
        assertThat(expectedProductCategory.getName()).isEqualTo(newProductCategoryDto.name());
        assertThat(expectedProductCategory.getDepartment()).isEqualTo(newProductCategoryDto.department());
    }

    @Test
    void testUpdateProductCategoryFromDto_ShouldUpdateExistingProductCategoryWithNewValues_WhenCalled() {
        // when
        mapper.updateProductCategoryFromDto(newProductCategoryDto, productCategory);

        // then
        assertThat(productCategory.getName()).isEqualTo(newProductCategoryDto.name());
        assertThat(productCategory.getDepartment()).isEqualTo(newProductCategoryDto.department());
    }
}
