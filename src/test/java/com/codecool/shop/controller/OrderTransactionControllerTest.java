package com.codecool.shop.controller;


import com.codecool.shop.dto.ordertransaction.NewOrderTransactionDto;
import com.codecool.shop.dto.ordertransaction.OrderTransactionDto;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.OrderTransactionService;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderTransactionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderTransactionService service;

    private UUID orderTransactionId;
    private UUID basketId;
    private UUID customerId;
    private OrderTransactionDto orderTransactionDto;
    private NewOrderTransactionDto newOrderTransactionDto;
    private String contentResponse;

    @BeforeEach
    void setUp() {
        orderTransactionId = UUID.fromString("421a22bc-66fb-4ee4-8d20-c5d532577822");
        basketId = UUID.fromString("1709b55a-7d56-45f8-9c5b-ac1c06a09120");
        customerId = UUID.fromString("76a76906-c153-42b0-b630-567044587c88");

        orderTransactionDto = new OrderTransactionDto(
                orderTransactionId,
                LocalDateTime.of(LocalDate.of(2012, 12, 12), LocalTime.of(12, 12, 12)),
                basketId,
                customerId
        );

        newOrderTransactionDto = new NewOrderTransactionDto(
                basketId,
                customerId
        );

        contentResponse = """
                [
                    {
                        "id": "421a22bc-66fb-4ee4-8d20-c5d532577822",
                        "submissionTime": "2012-12-12T12:12:12",
                        "basketId": "1709b55a-7d56-45f8-9c5b-ac1c06a09120",
                        "customerId": "76a76906-c153-42b0-b630-567044587c88"
                    }
                ]
                """;
    }

    @Test
    void testGetAllOrderTransactionsByCustomerId_ShouldReturnStatusOkAndListOfOrderTransactionDto_WhenExist()
            throws Exception {
        // when
        Mockito.when(service.getOrderTransactionByCustomerId(customerId)).thenReturn(List.of(orderTransactionDto));

        // then
        mockMvc.perform(get("/api/v1/order-transactions")
                        .param("customerId", customerId.toString()))
                .andExpectAll(status().isOk(),
                        content().json(contentResponse)
                );
    }

    @Test
    void testGetOrderTransactionById_ShouldReturnStatusNotFound_WhenNoCustomer() throws Exception {
        // when
        Mockito.when(service.getOrderTransactionByCustomerId(customerId))
                .thenThrow(new ObjectNotFoundException(customerId, Customer.class));

        // then
        mockMvc.perform(get("/api/v1/order-transactions")
                        .param("customerId", customerId.toString()))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

    @Test
    void testCreateNewOrderTransaction_ShouldReturnStatusCreatedAndOrderTransactionDto_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "basketId": "1709b55a-7d56-45f8-9c5b-ac1c06a09120",
                    "customerId": "76a76906-c153-42b0-b630-567044587c88"
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/order-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateNewOrderTransaction_ShouldReturnStatusNotFoundAndErrorMessages_WhenNoBasket() throws Exception {
        // given
        String contentRequest = """
                {
                    "basketId": "1709b55a-7d56-45f8-9c5b-ac1c06a09120",
                    "customerId": "76a76906-c153-42b0-b630-567044587c88"
                }
                """;
        // when
        Mockito.doThrow(new ObjectNotFoundException(basketId, Basket.class))
                .when(service).saveNewOrderTransaction(newOrderTransactionDto);

        // then
        mockMvc.perform(post("/api/v1/order-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Basket.class.getSimpleName() +
                                        " and id " + basketId + " does not exist")
                );
    }
    @Test
    void testCreateNewOrderTransaction_ShouldReturnStatusNotFoundAndErrorMessages_WhenNoCustomer() throws Exception {
        // given
        String contentRequest = """
                {
                    "basketId": "1709b55a-7d56-45f8-9c5b-ac1c06a09120",
                    "customerId": "76a76906-c153-42b0-b630-567044587c88"
                }
                """;
        // when
        Mockito.doThrow(new ObjectNotFoundException(customerId, Customer.class))
                .when(service).saveNewOrderTransaction(newOrderTransactionDto);

        // then
        mockMvc.perform(post("/api/v1/order-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

}