package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.dto.customer.EditCustomerNameDto;
import com.codecool.shop.dto.customer.EditCustomerPasswordDto;
import com.codecool.shop.dto.customer.NewCustomerDto;
import com.codecool.shop.repository.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    @Mapping(target = "submissionTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    Customer dtoToCustomer(NewCustomerDto newCustomerDto);

    @Mapping(target = "submissionTime", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateCustomerNameFromDto(EditCustomerNameDto editCustomerNameDto, @MappingTarget Customer customer);

    @Mapping(target = "submissionTime", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateCustomerPasswordFromDto(EditCustomerPasswordDto editCustomerPasswordDto, @MappingTarget Customer customer);
}
