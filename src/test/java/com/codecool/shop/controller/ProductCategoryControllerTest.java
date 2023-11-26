package com.codecool.shop.controller;

import com.codecool.shop.dto.productcategory.NewProductCategoryDto;
import com.codecool.shop.dto.productcategory.ProductCategoryDto;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.ProductCategory;
import com.codecool.shop.service.ProductCategoryService;
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

@WebMvcTest(controllers = ProductCategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductCategoryControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductCategoryService service;
    @MockBean
    ProductRepository productRepository;

    private UUID productCategoryId;
    private ProductCategoryDto productCategoryDto;
    private NewProductCategoryDto newProductCategoryDto;
    private String contentResponse;

    @BeforeEach
    void setUp() {
        productCategoryId = UUID.fromString("c59fdb01-14bc-4846-9238-cb1f7f68b2ca");

        productCategoryDto = new ProductCategoryDto(
                productCategoryId,
                "Name",
                "Department"
        );

        newProductCategoryDto = new NewProductCategoryDto(
                "Name",
                "Department"
        );

        contentResponse = """
                {
                    "id": "c59fdb01-14bc-4846-9238-cb1f7f68b2ca",
                    "name": "Name",
                    "department": "Department"
                }
                """;
    }

    @Test
    void testGetAllProductCategories_ShouldReturnStatusOkAndList_WhenCalled() throws Exception {
        // when
        Mockito.when(service.getProductCategories()).thenReturn(List.of());

        // then
        mockMvc.perform(get("/api/v1/product-categories"))
                .andExpectAll(status().isOk(),
                        content().json("[]")
                );
    }

    @Test
    void testGetProductCategoryById_ShouldReturnStatusOkAndProductCategoryDto_WhenExist() throws Exception {
        // when
        Mockito.when(service.getProductCategoryById(productCategoryId)).thenReturn(productCategoryDto);

        // then
        mockMvc.perform(get("/api/v1/product-categories/" + productCategoryId))
                .andExpectAll(status().isOk(),
                        content().json(contentResponse)
                );
    }

    @Test
    void testGetProductCategoryById_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoProductCategory()
            throws Exception {
        // when
        Mockito.when(service.getProductCategoryById(productCategoryId))
                .thenThrow(new ObjectNotFoundException(productCategoryId, ProductCategory.class));

        // then
        mockMvc.perform(get("/api/v1/product-categories/" + productCategoryId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + ProductCategory.class.getSimpleName() +
                                        " and id " + productCategoryId + " does not exist")
                );
    }

    @Test
    void testCreateNewProductCategory_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "department": "Department"
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/product-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateNewProductCategory_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues()
            throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "",
                    "department": ""
                }
                """;
        // then
        mockMvc.perform(post("/api/v1/product-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Name cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Department cannot be empty"))
                );
    }

    @Test
    void testUpdateProductCategory_ShouldReturnStatusCreated_WhenValidValues() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "department": "Department"
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/product-categories/" + productCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateProductCategory_ShouldReturnStatusBadRequestAndErrorMessages_WhenInvalidValues()
            throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "",
                    "department": ""
                }
                """;

        // then
        mockMvc.perform(put("/api/v1/product-categories/" + productCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isBadRequest(),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Name cannot be empty")),
                        jsonPath("$.errorMessage",
                                Matchers.containsString("Department cannot be empty"))
                );
    }

    @Test
    void testUpdateProductCategory_ShouldReturnStatusNotFound_WhenNoProductCategory() throws Exception {
        // given
        String contentRequest = """
                {
                    "name": "Name",
                    "department": "Department"
                }
                """;
        // when
        Mockito.doThrow(new ObjectNotFoundException(productCategoryId, ProductCategory.class))
                .when(service).updateProductCategory(productCategoryId, newProductCategoryDto);

        // then
        mockMvc.perform(put("/api/v1/product-categories/" + productCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + ProductCategory.class.getSimpleName() +
                                        " and id " + productCategoryId + " does not exist")
                );
    }

    @Test
    void testAssignProductsToProductCategory_ShouldReturnStatusNoContent_WhenExistAndValidValues() throws Exception {
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

        // then
        mockMvc.perform(put("/api/v1/product-categories/" + productCategoryId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAssignProductsToProductCategory_ShouldReturnStatusBadRequestAndErrorMessage_WhenNoProductCategory() throws Exception {
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
        Mockito.doThrow(new ObjectNotFoundException(productCategoryId, ProductCategory.class))
                .when(service).assignProductsToProductCategory(eq(productCategoryId), any(List.class));

        // then
        mockMvc.perform(put("/api/v1/product-categories/" + productCategoryId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + ProductCategory.class.getSimpleName() +
                                        " and id " + productCategoryId + " does not exist")
                );
    }

    @Test
    void testAssignProductsToProductCategory_ShouldReturnStatusBadRequestAndErrorMessage_WhenInvalidValues() throws Exception {
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
                .when(service).assignProductsToProductCategory(eq(productCategoryId), any(List.class));


        // then
        mockMvc.perform(put("/api/v1/product-categories/" + productCategoryId + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentRequest))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + Product.class.getSimpleName() +
                                        " and id " + productId + " does not exist")
                );
    }

    @Test
    void testDeleteProductCategory_ShouldReturnStatusNoContent_WhenProductCategoryExist() throws Exception {
        // then
        mockMvc.perform(delete("/api/v1/product-categories/" + productCategoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProductCategory_ShouldReturnStatusNotFoundAndErrorMessage_WhenNoProductCategory()
            throws Exception {
        // when
        Mockito.doThrow(new ObjectNotFoundException(productCategoryId, ProductCategory.class))
                .when(service).deleteProductCategory(productCategoryId);

        // then
        mockMvc.perform(delete("/api/v1/product-categories/" + productCategoryId))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.errorMessage")
                                .value("Object of a class " + ProductCategory.class.getSimpleName() +
                                        " and id " + productCategoryId + " does not exist")
                );
    }
}
