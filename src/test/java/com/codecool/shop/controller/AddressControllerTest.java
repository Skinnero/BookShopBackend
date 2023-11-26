package com.codecool.shop.controller;

import com.codecool.shop.dto.address.AddressDto;
import com.codecool.shop.dto.address.EditAddressDto;
import com.codecool.shop.dto.address.NewAddressDto;
import com.codecool.shop.repository.entity.Address;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.AddressService;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AddressService service;

    private UUID addressId;
    private UUID customerId;
    private AddressDto addressDto;
    private NewAddressDto newAddressDto;
    private EditAddressDto editAddressDto;
    private String contentResponse;

    @BeforeEach
    void setUp() {
        addressId = UUID.fromString("26fb399f-f8e3-4ccf-a409-25e6509c2239");
        customerId = UUID.fromString("783a660e-91ec-42f0-93d3-bbc2bb1484c0");

        addressDto = new AddressDto(
                addressId,
                123456L,
                "City",
                "Street",
                "17A",
                ""
        );

        newAddressDto = new NewAddressDto(
                123456L,
                "City",
                "Street",
                "17A",
                "",
                customerId
        );

        editAddressDto = new EditAddressDto(
                123456L,
                "City",
                "Street",
                "17A",
                ""
        );

        contentResponse = """
                {
                    "id": "26fb399f-f8e3-4ccf-a409-25e6509c2239",
                    "zipCode": 123456,
                    "city": "City",
                    "street": "Street",
                    "streetNumber": "17A",
                    "additionalInfo": ""
                }
                """;

    }

    @Test
    void testGetAddressByCustomerId_ShouldReturnStatusOKAndAddressDto_WhenCustomerExist() throws Exception {
        // when
        Mockito.when(service.getAddressByCustomerId(customerId)).thenReturn(addressDto);

        // then
        mockMvc.perform(get("/api/v1/addresses")
                        .param("customerId", customerId.toString()))
                .andExpectAll(status().isOk(),
                        content().json(contentResponse)
                );
    }

    @Test
    void testGetAddressByCustomerId_ShouldThrowStatusBadRequestAndErrorMessage_WhenNoCustomer() throws Exception {
        // when
        Mockito.doThrow(new ObjectNotFoundException(customerId, Customer.class))
                .when(service)
                .getAddressByCustomerId(customerId);

        // then
        mockMvc.perform(get("/api/v1/addresses")
                        .param("customerId", customerId.toString()))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

    @Test
    void testCreateNewAddress_ShouldReturnStatusCreatedAndAddressDto_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "zipCode": 123456,
                    "city": "City",
                    "street": "Street",
                    "streetNumber": "17A",
                    "additionalInfo": "",
                    "customerId": "783a660e-91ec-42f0-93d3-bbc2bb1484c0"
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateNewAddress_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoCustomer() throws Exception {
        // given
        String contentRequest = """
                {
                    "zipCode": 123456,
                    "city": "City",
                    "street": "Street",
                    "streetNumber": "17A",
                    "additionalInfo": "",
                    "customerId": "783a660e-91ec-42f0-93d3-bbc2bb1484c0"
                }
                """;

        // when
        Mockito.doThrow(new ObjectNotFoundException(customerId, Customer.class))
                        .when(service).saveNewAddress(newAddressDto);

        // then
        mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Customer.class.getSimpleName() +
                                        " and id " + customerId + " does not exist")
                );
    }

    @Test
    void testCreateNewAddress_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "zipCode": null,
                    "city": "",
                    "street": "",
                    "streetNumber": "",
                    "additionalInfo": "",
                    "customerId": ""
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Zip code cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("City cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Street cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Street number cannot be empty"))
                );
    }


    @Test
    void testUpdateAddress_ShouldReturnStatusCreatedAndAddressDto_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "zipCode": 123456,
                    "city": "City",
                    "street": "Street",
                    "streetNumber": "17A",
                    "additionalInfo": ""
                }
                """;

        // when
//        Mockito.when(service.updateAddress(addressId, editAddressDto)).thenReturn(addressDto);

        // then
        mockMvc.perform(put("/api/v1/addresses/" + addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateAddress_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoAddress() throws Exception {
        // given
        String contentRequest = """
                {
                    "zipCode": 123456,
                    "city": "City",
                    "street": "Street",
                    "streetNumber": "17A",
                    "additionalInfo": ""
                }
                """;

        // when
        Mockito.doThrow(new ObjectNotFoundException(addressId, Address.class))
                        .when(service).updateAddress(addressId, editAddressDto);
//        Mockito.when(service.updateAddress(addressId, editAddressDto))
//                .thenThrow(new ObjectNotFoundException(addressId, Address.class));

        // then
        mockMvc.perform(put("/api/v1/addresses/" + addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Address.class.getSimpleName() +
                                        " and id " + addressId + " does not exist")
                );
    }

    @Test
    void testUpdateAddress_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "zipCode": null,
                    "city": "",
                    "street": "",
                    "streetNumber": "",
                    "additionalInfo": ""
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/addresses/" + addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Zip code cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("City cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Street cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Street number cannot be empty"))
                );
    }

}
