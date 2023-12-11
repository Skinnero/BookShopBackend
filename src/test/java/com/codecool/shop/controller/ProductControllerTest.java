package com.codecool.shop.controller;

import com.codecool.shop.dto.product.NewProductDto;
import com.codecool.shop.dto.product.ProductDto;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.service.ProductService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService service;

    private UUID productId;
    private ProductDto productDto;
    private String contentResponse;

    @BeforeEach
    void setUp() {
        productId = UUID.fromString("882a7827-b9cd-4046-9d89-6b1504c902a2");

        productDto = new ProductDto(
                productId,
                "Name",
                "Description",
                BigDecimal.valueOf(12.3),
                "PLN"
        );

        contentResponse = """
                {
                    "id": "882a7827-b9cd-4046-9d89-6b1504c902a2",
                    "name": "Name",
                    "description": "Description",
                    "price": 12.3,
                    "currency": "PLN"
                }
                """;
    }

    @Test
    void testGetAllProducts_ShouldReturnStatusOkAndListOfProduct_WhenCalled() throws Exception {
        // when
        Mockito.when(service.getProducts()).thenReturn(List.of());

        // then
        mockMvc.perform(get("/api/v1/products"))
                .andExpectAll(status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void testGetProductById_ShouldReturnStatusOkAndProductDto_WhenExist() throws Exception {
        // when
        Mockito.when(service.getProductById(productId)).thenReturn(productDto);

        // then
        mockMvc.perform(get("/api/v1/products/" + productId))
                .andExpectAll(status().isOk(),
                        content().json(contentResponse)
                );
    }

    @Test
    void testGetProductById_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoProduct() throws Exception {
        // when
        Mockito.when(service.getProductById(productId))
                .thenThrow(new ObjectNotFoundException(productId, Product.class));

        // then
        mockMvc.perform(get("/api/v1/products/" + productId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Product.class.getSimpleName() +
                                        " and id " + productId + " does not exist")
                );
    }

    @Test
    void testCreateNewProduct_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "description": "Description",
                    "price": 12.3,
                    "currency": "PLN"
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isCreated());
    }

    @Test
    void testCreateNewProduct_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "",
                    "description": "",
                    "price": null,
                    "currency": ""
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage", Matchers.containsString("Name cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Description cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Price cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Currency cannot be empty"))
                );
    }

    @Test
    void testUpdateProduct_ShouldReturnStatusCreated_WhenValidValuesAndExist() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "description": "Description",
                    "price": 12.3,
                    "currency": "PLN"
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateProduct_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "",
                    "description": "",
                    "price": null,
                    "currency": ""
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage", Matchers.containsString("Name cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Description cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Price cannot be empty")),
                        jsonPath("$.errorMessage", Matchers.containsString("Currency cannot be empty"))
                );
    }

    @Test
    void testUpdateProduct_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoProduct() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "description": "Description",
                    "price": 12.3,
                    "currency": "PLN"
                }
                """;
        // when
        Mockito.doThrow(new ObjectNotFoundException(productId, Product.class))
                .when(service).updateProduct(eq(productId), any(NewProductDto.class));

        // then
        mockMvc.perform(put("/api/v1/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Product.class.getSimpleName() +
                                        " and id " + productId + " does not exist")
                );
    }

    @Test
    void testDeleteProduct_ShouldReturnStatusNoContent_WhenProductExist() throws Exception {
        // then
        mockMvc.perform(delete("/api/v1/products/" + productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_ShouldReturnStatusNotFound_WhenNoProduct() throws Exception {
        // when
        Mockito.doThrow(new ObjectNotFoundException(productId, Product.class))
                .when(service).deleteProduct(productId);

        // then
        mockMvc.perform(delete("/api/v1/products/" + productId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Product.class.getSimpleName() +
                                        " and id " + productId + " does not exist")
                );
    }

    @Test
    void testGetFilteredProducts_ShouldReturnStatusOkAndListOfProductDto_WhenCalled() throws Exception {
        // when
        Mockito.when(service.getFilteredProducts(any(UUID.class), any(UUID.class))).thenReturn(List.of());

        // then
        mockMvc.perform(get("/api/v1/products")
                        .param("supplierId", UUID.randomUUID().toString())
                        .param("productCategoryId", UUID.randomUUID().toString()))
                .andExpectAll(status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void testGetProductsFromSearchBar_ShouldReturnStatusOkAndListOfProductDto_WhenCalled() throws Exception {
        // when
        Mockito.when(service.getProductsFromSearchBar(anyString())).thenReturn(List.of());

        // then
        mockMvc.perform(get("/api/v1/products")
                        .param("name", "name"))
                .andExpectAll(status().isOk(),
                        content().json("[]")
                );
    }
}
