package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.address.AddressDto;
import com.codecool.shop.dto.address.EditAddressDto;
import com.codecool.shop.dto.address.NewAddressDto;
import com.codecool.shop.repository.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer.id", source = "customerId")
    Address dtoToAddress(NewAddressDto newAddressDto);

    @Mapping(target = "customer.id", source = "newAddressDto.customerId")
    Address dtoToAddress(UUID id, NewAddressDto newAddressDto);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateAddressFromDto(EditAddressDto editAddressDto, @MappingTarget Address address);

}
