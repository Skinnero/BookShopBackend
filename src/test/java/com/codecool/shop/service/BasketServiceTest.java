package com.codecool.shop.service;

import com.codecool.shop.dto.basket.BasketDto;
import com.codecool.shop.dto.basket.EditBasketDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.repository.BasketRepository;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.BasketMapper;
import com.codecool.shop.service.validator.BasketValidator;
import com.codecool.shop.service.validator.CustomerValidator;
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

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {
    @InjectMocks
    BasketService service;
    @Mock
    BasketRepository repository;
    @Mock
    BasketMapper mapper;
    @Mock
    BasketValidator validator;
    @Mock
    CustomerValidator customerValidator;
    @Mock
    ProductRepository productRepository;

    private UUID basketId;
    private UUID customerId;
    private UUID productId;
    private NewBasketDto newBasketDto;
    private EditBasketDto editBasketDto;
    private BasketDto basketDto;
    private Basket basket;
    private Product product;

    @BeforeEach
    void setUp() {
        basketId = UUID.fromString("783a660e-91ec-42f0-93d3-bbc2bb1484c0");
        customerId = UUID.fromString("b2212e0f-8124-44ce-a8d6-31ac5cfb75cb");
        productId = UUID.fromString("b2212e0f-8124-44ce-a8d6-31ac5cfb75cb");

        newBasketDto = new NewBasketDto(
                customerId
        );

        editBasketDto = new EditBasketDto(
                List.of(productId)
        );

        basketDto = new BasketDto(
                basketId,
                List.of()
        );

        product = new Product();

        basket = new Basket();
    }

    @Test
    void testGetBasketByCustomerId_ShouldReturnBasketDto_WhenExist() {
        // when
        Mockito.when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(basket));
        Mockito.when(mapper.toBasketDto(basket)).thenReturn(basketDto);

        BasketDto basketDtoFromTest = service.getBasketByCustomerId(customerId);

        //then
        assertThat(basketDtoFromTest).isEqualTo(basketDto);
    }

    @Test
    void testGetBasketByCustomerId_ShouldThrownObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(customerValidator).validateByEntityId(customerId);

        // then
        assertThatThrownBy(() -> service.getBasketByCustomerId(customerId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetBasketById_ShouldReturnBasketDto_WhenExist() {
        // when
        Mockito.when(repository.findById(basketId)).thenReturn(Optional.of(basket));
        Mockito.when(mapper.toBasketDto(basket)).thenReturn(basketDto);

        BasketDto basketDtoFromTest = service.getBasketById(basketId);

        //then
        assertThat(basketDtoFromTest).isEqualTo(basketDto);
    }

    @Test
    void testGetBasketById_ShouldThrownObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(repository).findById(basketId);

        // then
        assertThatThrownBy(() -> service.getBasketById(basketId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetAllBasketsWithProductsByCustomerId_ShouldReturnBasketDto_WhenExist() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(customerValidator).validateByEntityId(customerId);

        // then
        assertThatThrownBy(() -> service.getAllBasketsWithProductsByCustomerId(customerId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetAllBasketsWithProductsByCustomerId_ShouldReturnListOfBasketWithProductsDto_WhenBasketExist() {
        // when
        Mockito.when(repository.findAllByCustomerId(customerId)).thenReturn(List.of());

        List<BasketDto> basketDtoList = service.getAllBasketsWithProductsByCustomerId(customerId);

        // then
        assertThat(basketDtoList.size()).isEqualTo(0);
    }

    @Captor
    ArgumentCaptor<Basket> captor;

    @Test
    void testSaveNewBasket_ShouldSaveBasket_WhenCustomerExist() {
        // when
        Mockito.when(mapper.dtoToBasket(newBasketDto)).thenReturn(basket);
        service.saveNewBasket(newBasketDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(basket);
    }

    @Test
    void testSaveNewBasket_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(customerValidator).validateByEntityId(customerId);

        // then
        assertThatThrownBy(() -> service.saveNewBasket(newBasketDto)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Captor
    ArgumentCaptor<Basket> basketCaptor;

    @Test
    void testUpdateBasket_ShouldThrowObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.when(repository.findById(basketId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.updateBasket(basketId, editBasketDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testUpdateBasket_ShouldThrowObjectNotFoundException_WhenNoProduct() {
        // then
        assertThatThrownBy(() -> service.updateBasket(basketId, editBasketDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testUpdateBasket_ShouldUpdateListOfANewValue_WhenAllValid() {
        // when
        Mockito.when(repository.findById(basketId)).thenReturn(Optional.of(basket));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        service.updateBasket(basketId, editBasketDto);
        // then
        Mockito.verify(repository, Mockito.times(1)).save(basketCaptor.capture());
        assertThat(basketCaptor.getValue().getProducts().size()).isEqualTo(1);
    }

    @Test
    void testDeleteBasket_ShouldThrowObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(validator).validateByEntityId(basketId);

        // then
        assertThatThrownBy(() -> service.deleteBasket(basketId)).isInstanceOf(ObjectNotFoundException.class);

    }
}
