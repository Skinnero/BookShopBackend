package com.codecool.shop.service;

import com.codecool.shop.dto.product.NewProductDto;
import com.codecool.shop.dto.product.ProductDto;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.ProductMapper;
import com.codecool.shop.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    ProductService service;
    @Mock
    ProductRepository repository;
    @Mock
    ProductMapper mapper;
    @Mock
    ProductValidator validator;

    private UUID productId;
    private ProductDto productDto;
    private Product product;
    private NewProductDto newProductDto;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        productDto = new ProductDto(
                productId,
                "Name",
                "Description",
                BigDecimal.valueOf(12.3),
                "PLN"
        );

        product = new Product();

        newProductDto = new NewProductDto(
                "Name",
                "Description",
                BigDecimal.valueOf(12.3),
                "PLN"
        );
    }

    @Test
    void testGetProducts_ShouldReturnListOfProductDto_WhenCalled() {
        // when
        Mockito.when(repository.findAll()).thenReturn(List.of());
        List<ProductDto> list = service.getProducts();

        // then
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testGetProductById_ShouldReturnProductDto_WhenExist() {
        // when
        Mockito.when(validator.validateByEntityId(productId)).thenReturn(product);
        Mockito.when(mapper.toDto(product)).thenReturn(productDto);
        ProductDto expectedProductDto = service.getProductById(productId);

        // then
        assertThat(expectedProductDto).isEqualTo(productDto);
    }

    @Test
    void testGetProductById_ShouldThrowObjectNotFoundException_WhenNoProduct() {
        // when
        Mockito.when(validator.validateByEntityId(productId)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.getProductById(productId)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Captor
    ArgumentCaptor<Product> captor;

    @Test
    void testSaveNewProduct_ShouldReturnProductDto_WhenCalled() {
        // when
        Mockito.when(mapper.dtoToProduct(newProductDto)).thenReturn(product);
        service.saveNewProduct(newProductDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(product);
    }

    @Test
    void testUpdateProduct_ShouldReturnProductDto_WhenProductExist() {
        // when
        Mockito.when(validator.validateByEntityId(productId)).thenReturn(product);
        service.updateProduct(productId, newProductDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(product);
    }

    @Test
    void testUpdateProduct_ShouldThrowObjectNotFoundException_WhenNoProduct() {
        // when
        Mockito.when(validator.validateByEntityId(productId)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.updateProduct(productId, newProductDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testDeleteProduct_ShouldThrowObjectNotFoundException_WhenNoProduct() {
        // when
        Mockito.when(validator.validateByEntityId(productId)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.deleteProduct(productId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetFilteredProducts_ShouldReturnListOfProductDto_WhenCalled() {
        // when
        Mockito.when(repository.findAllBySupplierIdAndProductCategoryId(any(UUID.class), any(UUID.class)))
                .thenReturn(List.of());
        List<ProductDto> list = service.getFilteredProducts(UUID.randomUUID(), UUID.randomUUID());

        // then
        assertThat(list.size()).isEqualTo(0);

    }
}
