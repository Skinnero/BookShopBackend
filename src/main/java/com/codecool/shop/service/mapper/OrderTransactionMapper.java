package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.ordertransaction.NewOrderTransactionDto;
import com.codecool.shop.dto.ordertransaction.OrderTransactionDto;
import com.codecool.shop.repository.entity.OrderTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderTransactionMapper {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "basket.id", target = "basketId")
    OrderTransactionDto toDto(OrderTransaction transaction);

    @Mapping(target = "submissionTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "basketId", target = "basket.id")
    @Mapping(source = "customerId", target = "customer.id")
    OrderTransaction dtoToTransaction(NewOrderTransactionDto newTransactionDto);

}
