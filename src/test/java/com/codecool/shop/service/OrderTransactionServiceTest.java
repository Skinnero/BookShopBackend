package com.codecool.shop.service;

import com.codecool.shop.dto.ordertransaction.NewOrderTransactionDto;
import com.codecool.shop.dto.ordertransaction.OrderTransactionDto;
import com.codecool.shop.repository.OrderTransactionRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.repository.entity.OrderTransaction;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.OrderTransactionMapper;
import com.codecool.shop.service.validator.BasketValidator;
import com.codecool.shop.service.validator.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class OrderTransactionServiceTest {
    @InjectMocks
    OrderTransactionService service;
    @Mock
    OrderTransactionRepository repository;
    @Mock
    OrderTransactionMapper mapper;
    @Mock
    CustomerValidator customerValidator;
    @Mock
    BasketValidator basketValidator;

    private UUID customerId;
    private UUID basketId;
    private OrderTransaction orderTransaction;
    private OrderTransactionDto orderTransactionDto;
    private NewOrderTransactionDto newOrderTransactionDto;
    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        basketId = UUID.randomUUID();

        orderTransaction = new OrderTransaction(
                UUID.randomUUID(),
                LocalDateTime.of(LocalDate.of(2012, 12, 12), LocalTime.of(12, 12, 12)),
                new Basket(),
                new Customer()
        );

        orderTransactionDto = new OrderTransactionDto(
                orderTransaction.getId(),
                orderTransaction.getSubmissionTime(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        newOrderTransactionDto = new NewOrderTransactionDto(
                basketId,
                customerId
        );
    }

    @Test
    void testGetOrderTransactionByCustomerId_ShouldReturnListOfOrderTransactionDto_WhenExist() {
        // when
        Mockito.when(repository.findAllByCustomerId(customerId)).thenReturn(List.of(orderTransaction));
        Mockito.when(mapper.toDto(orderTransaction)).thenReturn(orderTransactionDto);
        List<OrderTransactionDto> orderTransactionDtoList = service.getOrderTransactionByCustomerId(customerId);

        // then
        assertThat(orderTransactionDtoList.size()).isEqualTo(1);
    }

    @Test
    void testGetOrderTransactionById_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(customerValidator).validateByEntityId(customerId);

        // then
        assertThatThrownBy(() -> service.getOrderTransactionByCustomerId(customerId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Captor
    ArgumentCaptor<OrderTransaction> captor;

    @Test
    void testSaveNewOrderTransaction_ShouldReturnOrderTransactionDtoWithNewDateValue_WhenCalled() {
        // given
        LocalDateTime dateTimeActual = orderTransaction.getSubmissionTime();

        // when
        Mockito.when(mapper.dtoToTransaction(newOrderTransactionDto)).thenReturn(orderTransaction);
        service.saveNewOrderTransaction(newOrderTransactionDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getSubmissionTime()).isNotEqualTo(dateTimeActual);
    }

    @Test
    void testSaveNewOrderTransaction_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(customerValidator).validateByEntityId(customerId);


        // then
        assertThatThrownBy(() -> service.saveNewOrderTransaction(newOrderTransactionDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testSaveNewOrderTransaction_ShouldThrowObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(basketValidator).validateByEntityId(basketId);

        // then
        assertThatThrownBy(() -> service.saveNewOrderTransaction(newOrderTransactionDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

}
