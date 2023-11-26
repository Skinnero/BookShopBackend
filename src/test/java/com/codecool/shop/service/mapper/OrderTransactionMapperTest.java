package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.ordertransaction.NewOrderTransactionDto;
import com.codecool.shop.dto.ordertransaction.OrderTransactionDto;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.repository.entity.OrderTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTransactionMapperTest {
    OrderTransactionMapper mapper = Mappers.getMapper(OrderTransactionMapper.class);

    private OrderTransaction orderTransaction;
    private NewOrderTransactionDto newOrderTransactionDto;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());

        Basket basket = new Basket();
        basket.setId(UUID.randomUUID());

        orderTransaction = new OrderTransaction(
                UUID.randomUUID(),
                LocalDateTime.of(LocalDate.of(2012, 12, 12), LocalTime.of(12, 12, 12)),
                basket,
                customer
        );

        newOrderTransactionDto = new NewOrderTransactionDto(
                basket.getId(),
                customer.getId()
        );
    }

    @Test
    void testToDto_ShouldReturnOrderTransactionDto_WhenCalled() {
        // when
        OrderTransactionDto expectedOrderTransactionDto = mapper.toDto(orderTransaction);

        // then
        assertThat(expectedOrderTransactionDto.id()).isEqualTo(orderTransaction.getId());
        assertThat(expectedOrderTransactionDto.submissionTime()).isEqualTo(orderTransaction.getSubmissionTime());
        assertThat(expectedOrderTransactionDto.basketId()).isEqualTo(orderTransaction.getBasket().getId());
        assertThat(expectedOrderTransactionDto.customerId()).isEqualTo(orderTransaction.getCustomer().getId());
    }

    @Test
    void testDtoToOrderTransaction_ShouldReturnOrderTransaction_WhenCalled() {
        // when
        OrderTransaction expectedOrderTransaction = mapper.dtoToTransaction(newOrderTransactionDto);

        // then
        assertThat(expectedOrderTransaction.getBasket().getId()).isEqualTo(newOrderTransactionDto.basketId());
        assertThat(expectedOrderTransaction.getCustomer().getId()).isEqualTo(newOrderTransactionDto.customerId());
    }
}
