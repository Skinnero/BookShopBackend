package com.codecool.shop.controller;

import com.codecool.shop.dto.supplier.NewSupplierDto;
import com.codecool.shop.dto.supplier.SupplierDto;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.Supplier;
import com.codecool.shop.service.SupplierService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SupplierController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SupplierControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    SupplierService service;

    private UUID supplierId;
    private SupplierDto supplierDto;
    private String contentResponse;

    @BeforeEach
    void setUp() {
        supplierId = UUID.fromString("46e8ad54-9c8c-4296-a0df-cf79924eebd9");

        supplierDto = new SupplierDto(
                supplierId,
                "Name",
                "Description"
        );

        contentResponse = """
                {
                    "id": "46e8ad54-9c8c-4296-a0df-cf79924eebd9",
                    "name": "Name",
                    "description": "Description"
                }
                """;
    }

    @Test
    void testGetAllSuppliers_ShouldReturnStatusOkAndList_WhenCalled() throws Exception {
        // when
        Mockito.when(service.getSuppliers()).thenReturn(List.of());

        // then
        mockMvc.perform(get("/api/v1/suppliers"))
                .andExpectAll(status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void testGetSupplierById_ShouldReturnStatusOkAndSupplierDto_WhenExist() throws Exception {
        // when
        Mockito.when(service.getSupplierById(supplierId)).thenReturn(supplierDto);

        // then
        mockMvc.perform(get("/api/v1/suppliers/" + supplierId))
                .andExpectAll(status().isOk(),
                        content().json(contentResponse)
                );
    }

    @Test
    void testGetSupplierById_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoProductCategory() throws Exception {
        // when
        Mockito.when(service.getSupplierById(supplierId))
                .thenThrow(new ObjectNotFoundException(supplierId, Supplier.class));

        // then
        mockMvc.perform(get("/api/v1/suppliers/" + supplierId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Supplier.class.getSimpleName() +
                                        " and id " + supplierId + " does not exist")
                );
    }

    @Test
    void testCreateNewSupplier_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "description": "Description"
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateNewSupplier_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "",
                    "description": ""
                }
                """;
        // then
        mockMvc.perform(post("/api/v1/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage", Matchers.containsString("Name cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Description cannot be empty"))
                );
    }

    @Test
    void testUpdateSupplier_ShouldReturnStatusCreated_WhenValidValuesAndExist() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "description": "Description"
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/suppliers/" + supplierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateSupplier_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "",
                    "description": ""
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/suppliers/" + supplierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage", Matchers.containsString("Name cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Description cannot be empty"))
                );
    }

    @Test
    void testUpdateSupplier_ShouldReturnStatusNotFoundAndErrorMessages_WhenValidValuesAndNoSupplier()
            throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "description": "Description"
                }
                """;
        // when
        Mockito.doThrow(new ObjectNotFoundException(supplierId, Supplier.class))
                .when(service).updateSupplier(eq(supplierId), any(NewSupplierDto.class));

        // then
        mockMvc.perform(put("/api/v1/suppliers/" + supplierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Supplier.class.getSimpleName() +
                                        " and id " + supplierId + " does not exist")
                );
    }

    @Test
    void testAssignProductsToSupplier_ShouldReturnStatusNoContent_WhenExistAndValidValues() throws Exception {
        // given
        String contentRequest = """
                [
                    {
                        "id": "4f368515-18d6-472e-99a7-da5f3a68517d"
                    },
                    {
                        "id": "9ec8e642-f589-4ed5-bd5e-c37ec7edf37d"
                    }
                ]
                """;

        // then
        mockMvc.perform(put("/api/v1/suppliers/" + supplierId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAssignProductsToSupplier_ShouldReturnStatusBadRequestAndErrorMessage_WhenNoProductCategory() throws Exception {
        // given
        String contentRequest = """
                [
                    {
                        "id": "4f368515-18d6-472e-99a7-da5f3a68517d"
                    },
                    {
                        "id": "9ec8e642-f589-4ed5-bd5e-c37ec7edf37d"
                    }
                ]
                """;

        // when
        Mockito.doThrow(new ObjectNotFoundException(supplierId, Supplier.class))
                .when(service).assignProductsToSupplier(any(UUID.class), any(List.class));

        // then
        mockMvc.perform(put("/api/v1/suppliers/" + supplierId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Supplier.class.getSimpleName() +
                                        " and id " + supplierId + " does not exist")
                );
    }

    @Test
    void testAssignProductsToSupplier_ShouldReturnStatusBadRequestAndErrorMessage_WhenInvalidValues() throws Exception {
        // given
        UUID productId = UUID.randomUUID();

        String contentRequest = """
                [
                    {
                        "id": "4f368515-18d6-472e-99a7-da5f3a68517d"
                    },
                    {
                        "id": "9ec8e642-f589-4ed5-bd5e-c37ec7edf37d"
                    }
                ]
                """;
        // when
        Mockito.doThrow(new ObjectNotFoundException(productId, Product.class))
                .when(service).assignProductsToSupplier(any(UUID.class), any(List.class));

        // then
        mockMvc.perform(put("/api/v1/suppliers/" + supplierId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Product.class.getSimpleName() +
                                        " and id " + productId + " does not exist")
                );
    }

    @Test
    void testDeleteSupplier_ShouldReturnStatusNoContent_WhenExist() throws Exception {
        // then
        mockMvc.perform(delete("/api/v1/suppliers/" + supplierId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSupplier_ShouldReturnStatusNoContent_WhenNoSupplier() throws Exception {
        // when
        Mockito.doThrow(new ObjectNotFoundException(supplierId, Supplier.class))
                .when(service).deleteSupplier(supplierId);

        // then
        mockMvc.perform(delete("/api/v1/suppliers/" + supplierId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Supplier.class.getSimpleName() +
                                        " and id " + supplierId + " does not exist")
                );
    }
}
