package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.product.NewProductDto;
import com.codecool.shop.dto.product.ProductDto;
import com.codecool.shop.repository.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest {
    ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    private UUID productId;
    private Product product;
    private NewProductDto newProductDto;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        product = new Product(
                productId,
                "Name",
                "Department",
                BigDecimal.valueOf(12.3),
                "PLN"
        );

        newProductDto = new NewProductDto(
                "Name2",
                "Department2",
                BigDecimal.valueOf(12.3),
                "PLN"
        );
    }

    @Test
    void testToDto_ShouldReturnProductDto_WhenCalled() {
        // when
        ProductDto expectedProductDto = mapper.toDto(product);

        // then
        assertThat(expectedProductDto.id()).isEqualTo(product.getId());
        assertThat(expectedProductDto.name()).isEqualTo(product.getName());
        assertThat(expectedProductDto.description()).isEqualTo(product.getDescription());
        assertThat(expectedProductDto.price()).isEqualTo(product.getPrice());
        assertThat(expectedProductDto.currency()).isEqualTo(product.getCurrency());
    }

    @Test
    void testDtoToProduct_ShouldReturnProduct_WhenCalledWithNewProductDto() {
        // when
        Product expectedProduct = mapper.dtoToProduct(newProductDto);

        // then
        assertThat(expectedProduct.getName()).isEqualTo(newProductDto.name());
        assertThat(expectedProduct.getDescription()).isEqualTo(newProductDto.description());
        assertThat(expectedProduct.getPrice()).isEqualTo(newProductDto.price());
        assertThat(expectedProduct.getCurrency()).isEqualTo(newProductDto.currency());

    }

    @Test
    void testDtoToProduct_ShouldReturnProduct_WhenCalledWithId() {
        // when
        Product expectedProduct = mapper.dtoToProduct(productId);

        // then
        assertThat(expectedProduct.getId()).isEqualTo(productId);
    }

    @Test
    void testUpdateProductFromDto_ShouldUpdateProductWithNewValues_WhenCalled() {
        // when
        mapper.updateProductFromDto(newProductDto, product);

        // then
        assertThat(product.getName()).isEqualTo(newProductDto.name());
        assertThat(product.getDescription()).isEqualTo(newProductDto.description());
        assertThat(product.getPrice()).isEqualTo(newProductDto.price());
        assertThat(product.getCurrency()).isEqualTo(newProductDto.currency());
    }
}
