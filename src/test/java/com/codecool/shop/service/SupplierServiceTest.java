package com.codecool.shop.service;

import com.codecool.shop.dto.product.ProductIdDto;
import com.codecool.shop.dto.supplier.NewSupplierDto;
import com.codecool.shop.dto.supplier.SupplierDto;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.SupplierRepository;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.repository.entity.Supplier;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.SupplierMapper;
import com.codecool.shop.service.validator.SupplierValidator;
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
public class SupplierServiceTest {
    @InjectMocks
    SupplierService service;
    @Mock
    SupplierRepository repository;
    @Mock
    SupplierMapper mapper;
    @Mock
    SupplierValidator validator;
    @Mock
    ProductRepository productRepository;

    private UUID supplierId;
    private Supplier supplier;
    private SupplierDto supplierDto;
    private NewSupplierDto newSupplierDto;

    @BeforeEach
    void setUp() {
        supplierId = UUID.randomUUID();

        supplier = new Supplier();

        supplierDto = new SupplierDto(
                supplierId,
                "Name",
                "Description"
        );

        newSupplierDto = new NewSupplierDto(
                "Name",
                "Description"
        );
    }

    @Test
    void testGetSuppliers_ShouldReturnListOfSupplierDto_WhenCalled() {
        // when
        Mockito.when(repository.findAll()).thenReturn(List.of());
        List<SupplierDto> list = service.getSuppliers();

        // then
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testGetSupplierById_ShouldReturnSupplierDto_WhenExist() {
        // when
        Mockito.when(repository.findById(supplierId)).thenReturn(Optional.of(supplier));
        Mockito.when(mapper.toDto(supplier)).thenReturn(supplierDto);
        SupplierDto expectedSupplierDto = service.getSupplierById(supplierId);

        // then
        assertThat(expectedSupplierDto).isEqualTo(supplierDto);
    }

    @Test
    void testGetSupplierById_ShouldThrowObjectNotFoundException_WhenNoProductCategory() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(repository).findById(supplierId);

        // then
        assertThatThrownBy(() -> service.getSupplierById(supplierId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Captor
    ArgumentCaptor<Supplier> captor;

    @Test
    void testSaveNewSupplier_ShouldReturnSupplierDto_WhenCalled() {
        // when
        Mockito.when(mapper.dtoToSupplier(newSupplierDto)).thenReturn(supplier);
        service.saveNewSupplier(newSupplierDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(supplier);
    }

    @Test
    void testUpdateSupplier_ShouldReturnSupplierDto_WhenCalled() {
        // when
        Mockito.when(repository.findById(supplierId)).thenReturn(Optional.of(supplier));
        service.updateSupplier(supplierId, newSupplierDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(supplier);
    }

    @Test
    void testDeleteSupplier_ShouldThrowObjectNotFoundException_WhenNoSupplier() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class)
                .when(validator).validateByEntityId(supplierId);

        // then
        assertThatThrownBy(() -> service.deleteSupplier(supplierId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testAssignProductsToSupplier_ShouldAssignProductsToSupplier_WhenExist() {
        // given
        ProductIdDto productIdDtoOne = new ProductIdDto(UUID.randomUUID());
        ProductIdDto productIdDtoTwo = new ProductIdDto(UUID.randomUUID());

        Product productOne = new Product();
        productOne.setId(productIdDtoOne.id());

        Product productTwo = new Product();
        productTwo.setId(productIdDtoTwo.id());

        // when
        Mockito.when(repository.findById(supplierId)).thenReturn(Optional.of(supplier));
        Mockito.when(productRepository.findById(productIdDtoOne.id())).thenReturn(Optional.of(productOne));
        Mockito.when(productRepository.findById(productIdDtoTwo.id())).thenReturn(Optional.of(productTwo));
        service.assignProductsToSupplier(supplierId, List.of(productIdDtoOne, productIdDtoTwo));

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getProducts().size()).isEqualTo(2);
    }

    @Test
    void testAssignProductsToSupplier_ShouldThrowObjectNotFoundException_WhenNoSupplier() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(repository).findById(supplierId);

        // then
        assertThatThrownBy(() -> service.assignProductsToSupplier(supplierId, List.of()))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testAssignProductsToSupplier_ShouldThrowObjectNotFoundException_WhenNoProduct() {
        // given
        ProductIdDto productIdDto = new ProductIdDto(UUID.randomUUID());

        // when
        Mockito.when(repository.findById(supplierId)).thenReturn(Optional.of(supplier));
        Mockito.doThrow(ObjectNotFoundException.class).when(productRepository).findById(any());

        // then
        assertThatThrownBy(() -> service.assignProductsToSupplier(supplierId, List.of(productIdDto)))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}
