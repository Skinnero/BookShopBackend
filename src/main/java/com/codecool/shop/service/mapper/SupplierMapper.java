package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.supplier.NewSupplierDto;
import com.codecool.shop.dto.supplier.SupplierDto;
import com.codecool.shop.repository.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDto toDto(Supplier supplier);

    @Mapping(target = "id", ignore = true)
    Supplier dtoToSupplier(NewSupplierDto newSupplierDto);

    @Mapping(target = "id", ignore = true)
    void updateSupplierFromDto(NewSupplierDto newSupplierDto, @MappingTarget Supplier supplier);
}
