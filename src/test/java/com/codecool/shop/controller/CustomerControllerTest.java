package com.codecool.shop.controller;

import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.CustomerService;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CustomerService service;

    private UUID customerId;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerId = UUID.fromString("783a660e-91ec-42f0-93d3-bbc2bb1484c0");

        customerDto = new CustomerDto(
                customerId,
                "Name",
                "Email@wp.pl",
                LocalDate.of(2012, 12, 12)
        );

    }

    @Test
    void testGetCustomerById_ShouldReturnStatusOkAndCustomerDto_WhenCustomerExist() throws Exception {
        // given
        String contentResponse = """
                {
                    "id": "783a660e-91ec-42f0-93d3-bbc2bb1484c0",
                    "name": "Name",
                    "email": "Email@wp.pl",
                    "submissionTime": "2012-12-12"
                }
                """;

        // when
        Mockito.when(service.getCustomerById(customerId)).thenReturn(customerDto);

        // then
        mockMvc.perform(get("/api/v1/customers/" + customerId))
                .andExpectAll(status().isOk(),
                        content().json(contentResponse)
                );
    }

    @Test
    void testGetCustomerById_ShouldThrowStatusNotFoundAndErrorMessage_WhenNoCustomer() throws Exception {
        // when
        Mockito.doThrow(new ObjectNotFoundException(customerId, Customer.class)).when(service).getCustomerById(customerId);

        // then
        mockMvc.perform(get("/api/v1/customers/" + customerId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

    @Test
    void testUpdateCustomerName_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Kacper"
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/customers/" + customerId + "/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateCustomerName_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": ""
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/customers/" + customerId + "/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Name cannot be empty"))
                );
    }
    @Test
    void testUpdateCustomerPassword_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "password": "Confidential"
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/customers/" + customerId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateCustomerPassword_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "password": ""
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/customers/" + customerId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Password cannot be empty"))
                );
    }

    @Test
    void testDeleteCustomer_ShouldReturnStatusNoContent_WhenCustomerExist() throws Exception {
        // then
        mockMvc.perform(delete("/api/v1/customers/" + customerId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCustomer_ShouldReturnStatusNoFound_WhenNoCustomer() throws Exception {
        // when
        Mockito.doThrow(new ObjectNotFoundException(customerId, Customer.class))
                        .when(service).softDeleteCustomer(customerId);

        // then
        mockMvc.perform(delete("/api/v1/customers/" + customerId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }
}
