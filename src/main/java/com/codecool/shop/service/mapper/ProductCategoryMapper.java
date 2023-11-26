package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.productcategory.NewProductCategoryDto;
import com.codecool.shop.dto.productcategory.ProductCategoryDto;
import com.codecool.shop.repository.entity.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    ProductCategoryDto toDto(ProductCategory productCategory);

    @Mapping(target = "id", ignore = true)
    ProductCategory dtoToProductCategory(NewProductCategoryDto newProductCategoryDto);

    @Mapping(target = "id", ignore = true)
    void updateProductCategoryFromDto(NewProductCategoryDto newProductCategoryDto,
                                      @MappingTarget ProductCategory productCategory);

}
