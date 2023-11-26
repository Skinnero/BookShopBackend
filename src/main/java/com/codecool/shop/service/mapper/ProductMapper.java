package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.product.NewProductDto;
import com.codecool.shop.dto.product.ProductDto;
import com.codecool.shop.repository.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product dtoToProduct(NewProductDto newProductDto);

    @Mapping(target = "price", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "id", source = "id")
    Product dtoToProduct(UUID id);

    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(NewProductDto newProductDto, @MappingTarget Product product);
}
