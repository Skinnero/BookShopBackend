package com.codecool.shop.controller;

import com.codecool.shop.dto.basket.BasketDto;
import com.codecool.shop.dto.basket.EditBasketDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.BasketService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BasketController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BasketControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BasketService service;
    @MockBean
    CustomerRepository customerRepository;

    private UUID basketId;
    private UUID customerId;
    private BasketDto basketDto;
    private EditBasketDto editBasketDto;
    private String contentResponse;

    @BeforeEach
    void setUp() {
        basketId = UUID.fromString("e2cb5e73-3637-4614-9067-49fde8406969");
        customerId = UUID.fromString("1dfdd491-cbf9-4c21-aa96-a08ca97d4a2f");

        basketDto = new BasketDto(
                basketId,
                List.of()
        );

        editBasketDto = new EditBasketDto(
                List.of()
        );

        contentResponse = """
                {
                    "basketId": "e2cb5e73-3637-4614-9067-49fde8406969",
                    "products": []
                }
                """;
    }

    @Test
    void testGetBasketByCustomerId_ShouldReturnStatusOkAndBasketDto_WhenExist() throws Exception {
        // when
        Mockito.when(service.getBasketByCustomerId(customerId)).thenReturn(basketDto);

        // then
        mockMvc.perform(get("/api/v1/baskets")
                        .param("customerId", String.valueOf(customerId)))
                .andExpectAll(status().isOk(),
                        content().json(contentResponse)
                );
    }

    @Test
    void testGetBasketByCustomerId_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoCustomer() throws Exception {
        // when
        Mockito.when(service.getBasketByCustomerId(customerId))
                .thenThrow(new ObjectNotFoundException(customerId, Customer.class));

        // then
        mockMvc.perform(get("/api/v1/baskets")
                        .param("customerId", String.valueOf(customerId)))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

    @Test
    void testGetBasketWithProductsById_ShouldReturnStatusOkAndBasketWithProductsDto_WhenExist() throws Exception {
        // when
        Mockito.when(service.getAllBasketsWithProductsByCustomerId(customerId)).thenReturn(List.of());

        // then
        mockMvc.perform(get("/api/v1/baskets/detailed")
                        .param("customerId", String.valueOf(customerId)))
                .andExpectAll(status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void testGetBasketWithProductsById_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoBasket() throws Exception {
        // when
        Mockito.when(service.getAllBasketsWithProductsByCustomerId(customerId))
                .thenThrow(new ObjectNotFoundException(basketId, Basket.class));

        // then
        mockMvc.perform(get("/api/v1/baskets/detailed")
                        .param("customerId", String.valueOf(customerId)))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Basket.class.getSimpleName() +
                                        " and id " + basketId + " does not exist")
                );
    }

    @Test
    void testGetBasketWithProductsById_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoCustomer() throws Exception {
        // when
        Mockito.when(service.getAllBasketsWithProductsByCustomerId(customerId))
                .thenThrow(new ObjectNotFoundException(customerId, Customer.class));

        // then
        mockMvc.perform(get("/api/v1/baskets/detailed")
                        .param("customerId", String.valueOf(customerId)))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

    @Test
    void testCreateNewBasket_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "customerId": "39332501-f73c-4788-a690-bd05364870c0"
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateNewBasket_ShouldReturnStatusNotFoundAndErrorMessages_WhenNoCustomer() throws Exception {
        // given
        String contentRequest = """
                {
                    "customerId": ""
                }
                """;

        // when
        Mockito.doThrow(new ObjectNotFoundException(customerId, Customer.class))
                .when(service).saveNewBasket(any(NewBasketDto.class));

        // then
        mockMvc.perform(post("/api/v1/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

    @Test
    void testUpdateBasket_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "products": []
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/baskets/" + basketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateBasket_ShouldReturnStatusNotFoundAndErrorMessages_WhenNoBasket() throws Exception {
        // given
        String contentRequest = """
                {
                    "products": []
                }
                """;
        // when
        Mockito.doThrow(new ObjectNotFoundException(basketId, Basket.class))
                .when(service).updateBasket(basketId, editBasketDto);

        // then
        mockMvc.perform(put("/api/v1/baskets/" + basketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Basket.class.getSimpleName() +
                                        " and id " + basketId + " does not exist")
                );
    }

    @Test
    void testDeleteBasket_ShouldReturnStatusNoContent_WhenBasketExist() throws Exception {
        // then
        mockMvc.perform(delete("/api/v1/baskets/" + basketId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBasket_ShouldReturnStatusNotFound_WhenNoBasket() throws Exception {
        // when
        Mockito.doThrow(new ObjectNotFoundException(basketId, Basket.class))
                .when(service).deleteBasket(basketId);

        // then
        mockMvc.perform(delete("/api/v1/baskets/" + basketId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Basket.class.getSimpleName() +
                                        " and id " + basketId + " does not exist")
                );
    }
}
