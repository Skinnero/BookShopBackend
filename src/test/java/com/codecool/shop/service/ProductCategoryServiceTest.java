package com.codecool.shop.service;

import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.productcategory.NewProductCategoryDto;
import com.codecool.shop.dto.productcategory.ProductCategoryDto;
import com.codecool.shop.repository.ProductCategoryRepository;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.ProductCategory;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.ProductCategoryMapper;
import com.codecool.shop.service.validator.ProductCategoryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryServiceTest {
    @InjectMocks
    ProductCategoryService service;
    @Mock
    ProductCategoryRepository repository;
    @Mock
    ProductCategoryMapper mapper;
    @Mock
    ProductCategoryValidator validator;
    @Mock
    ProductRepository productRepository;

    private UUID productCategoryId;
    private ProductCategory productCategory;
    private ProductCategoryDto productCategoryDto;
    private NewProductCategoryDto newProductCategoryDto;

    @BeforeEach
    void setUp() {
        productCategoryId = UUID.randomUUID();

        productCategory = new ProductCategory();

        productCategoryDto = new ProductCategoryDto(
                productCategoryId,
                "Name",
                "Department"
        );

        newProductCategoryDto = new NewProductCategoryDto(
                "Name",
                "Department"
        );
    }

    @Test
    void testGetProductCategories_ShouldReturnListOfProductCategoryDto_WhenCalled() {
        // when
        Mockito.when(repository.findAll()).thenReturn(List.of());
        List<ProductCategoryDto> list = service.getProductCategories();

        // then
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testGetProductCategoryById_ShouldReturnProductCategoryDto_WhenExist() {
        // when
        Mockito.when(repository.findById(productCategoryId)).thenReturn(Optional.of(productCategory));
        Mockito.when(mapper.toDto(productCategory)).thenReturn(productCategoryDto);
        ProductCategoryDto expectedProductCategoryDto = service.getProductCategoryById(productCategoryId);

        // then
        assertThat(expectedProductCategoryDto).isEqualTo(productCategoryDto);
    }

    @Test
    void testGetProductCategoryById_ShouldThrowObjectNotFoundException_WhenNoProductCategory() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(repository).findById(productCategoryId);

        // then
        assertThatThrownBy(() -> service.getProductCategoryById(productCategoryId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Captor
    ArgumentCaptor<ProductCategory> captor;

    @Test
    void testSaveNewProductCategory_ShouldReturnProductCategoryDto_WhenCalled() {
        // when
        Mockito.when(mapper.dtoToProductCategory(newProductCategoryDto)).thenReturn(productCategory);
        service.saveNewProductCategory(newProductCategoryDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(productCategory);
    }

    @Test
    void testUpdateProductCategory_ShouldSaveProductCategory_WhenExist() {
        // when
        Mockito.when(repository.findById(productCategoryId)).thenReturn(Optional.of(productCategory));
        service.updateProductCategory(productCategoryId, newProductCategoryDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(productCategory);
    }

    @Test
    void testUpdateProductCategory_ShouldThrowObjectNotFoundException_WhenNoProductCategory() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(repository).findById(productCategoryId);

        // then
        assertThatThrownBy(() -> service.updateProductCategory(productCategoryId, newProductCategoryDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testDeleteProductCategory_ShouldThrowObjectNotFoundException_WhenNoProductCategory() {
        Mockito.doThrow(ObjectNotFoundException.class).when(validator).validateByEntityId(productCategoryId);


        // then
        assertThatThrownBy(() -> service.deleteProductCategory(productCategoryId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testAssignProductsToProductCategory_ShouldAssignProductsToProductCategory_WhenExist() {
        // given
        ProductIdDto productIdDtoOne = new ProductIdDto(UUID.randomUUID());
        ProductIdDto productIdDtoTwo = new ProductIdDto(UUID.randomUUID());

        Product productOne = new Product();
        productOne.setId(productIdDtoOne.id());

        Product productTwo = new Product();
        productTwo.setId(productIdDtoTwo.id());

        // when
        Mockito.when(repository.findById(productCategoryId)).thenReturn(Optional.of(productCategory));
        Mockito.when(productRepository.findById(productIdDtoOne.id())).thenReturn(Optional.of(productOne));
        Mockito.when(productRepository.findById(productIdDtoTwo.id())).thenReturn(Optional.of(productTwo));
        service.assignProductsToProductCategory(productCategoryId, List.of(productIdDtoOne, productIdDtoTwo));

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getProducts().size()).isEqualTo(2);
    }

    @Test
    void testAssignProductsToProductCategory_ShouldAssignProductsToProductCategory_WheNoProductCategory() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(repository).findById(productCategoryId);

        // then
        assertThatThrownBy(() -> service.assignProductsToProductCategory(productCategoryId, List.of()))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testAssignProductsToProductCategory_ShouldThrowObjectNotFoundException_WhenNoProduct() {
        // given
        ProductIdDto productIdDto = new ProductIdDto(UUID.randomUUID());

        // when
        Mockito.when(repository.findById(productCategoryId)).thenReturn(Optional.of(productCategory));
        Mockito.doThrow(ObjectNotFoundException.class).when(productRepository).findById(any());

        // then
        assertThatThrownBy(() -> service.assignProductsToProductCategory(productCategoryId, List.of(productIdDto)))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}
