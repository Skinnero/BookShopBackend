package com.codecool.shop.service;

import com.codecool.shop.dto.basket.BasketWithProductsDto;
import com.codecool.shop.dto.basket.EditBasketDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.dto.basket.ProductsInBasketDto;
import com.codecool.shop.repository.BasketRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.projection.BasketProjection;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.BasketMapper;
import com.codecool.shop.service.validator.BasketValidator;
import com.codecool.shop.service.validator.CustomerValidator;
import com.codecool.shop.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    ProductValidator productValidator;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private BasketProjection basketProjection = factory.createProjection(BasketProjection.class);
    private BasketWithProductsDto basketWithProductsDto;
    private UUID basketId;
    private UUID customerId;
    private UUID productId;
    private NewBasketDto newBasketDto;
    private EditBasketDto editBasketDto;
    private Basket basket;

    @BeforeEach
    void setUp() {
        basketId = UUID.fromString("783a660e-91ec-42f0-93d3-bbc2bb1484c0");
        customerId = UUID.fromString("b2212e0f-8124-44ce-a8d6-31ac5cfb75cb");
        productId = UUID.fromString("b2212e0f-8124-44ce-a8d6-31ac5cfb75cb");

        newBasketDto = new NewBasketDto(
                customerId
        );

        editBasketDto = new EditBasketDto(
                List.of(new ProductsInBasketDto(productId, 2))
        );

        basket = new Basket();

        basketWithProductsDto = new BasketWithProductsDto(
                basketId,
                List.of()
        );
    }

    @Test
    void testGetBasketWithProductsById_ShouldReturnBasketWithProductsDto_WhenBasketExist() {
        // when
        Mockito.when(repository.findProductsInBasketById(basketId)).thenReturn(List.of(basketProjection));
        Mockito.when(mapper.toBasketWithProductsDto(basketId, List.of(basketProjection)))
                .thenReturn(basketWithProductsDto);
        BasketWithProductsDto expectedBasketWithProductsDto = service.getBasketWithProductsById(basketId);

        // then
        assertThat(expectedBasketWithProductsDto).isEqualTo(basketWithProductsDto);
    }

    @Test
    void testGetBasketWithProductsById_ShouldThrownObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(validator).validateByEntityId(basketId);

        // then
        assertThatThrownBy(() -> service.getBasketWithProductsById(basketId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
    @Test
    void testGetBasketWithProductsByCustomerId_ShouldThrownObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(customerValidator).validateByEntityId(customerId);

        // then
        assertThatThrownBy(() -> service.getAllBasketsWithProductsByCustomerId(customerId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetAllBasketsWithProductsByCustomerId_ShouldReturnListOfBasketWithProductsDto_WhenBasketExist() {
        // given
        BasketProjection basketProjectionTwo = factory.createProjection(BasketProjection.class);
        basketProjectionTwo.setBasketId(basketId);
        UUID differentBasketId = UUID.randomUUID();
        basketProjection.setBasketId(differentBasketId);

        // when
        Mockito.when(repository.findProductsInBasketByCustomerId(customerId))
                .thenReturn(List.of(basketProjection, basketProjectionTwo));
        Mockito.when(mapper.toBasketWithProductsDto(basketId, List.of(basketProjectionTwo)))
                .thenReturn(basketWithProductsDto);
        Mockito.when(mapper.toBasketWithProductsDto(differentBasketId, List.of(basketProjection)))
                .thenReturn(basketWithProductsDto);
        List<BasketWithProductsDto> list = service.getAllBasketsWithProductsByCustomerId(customerId);

        // then
        assertThat(list.size()).isEqualTo(2);
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
    ArgumentCaptor<List<UUID>> uuidListCaptor;

    @Test
    void testUpdateBasket_ShouldUpdateBasketWithNewList_WhenBasketExist() {
        // when
        service.updateBasket(basketId, editBasketDto);

        // then
        Mockito.verify(mapper, Mockito.times(1))
                .dtoToBasket(eq(basketId), uuidListCaptor.capture());
        assertThat(uuidListCaptor.getValue().size()).isEqualTo(2);
    }

    @Test
    void testUpdateBasket_ShouldThrowObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(validator).validateByEntityId(basketId);

        // then
        assertThatThrownBy(() -> service.updateBasket(basketId, editBasketDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testUpdateBasket_ShouldThrowObjectNotFoundException_WhenNoProduct() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(productValidator).validateByEntityId(any(UUID.class));

        // then
        assertThatThrownBy(() -> service.updateBasket(basketId, editBasketDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testDeleteBasket_ShouldThrowObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(validator).validateByEntityId(basketId);

        // then
        assertThatThrownBy(() -> service.deleteBasket(basketId)).isInstanceOf(ObjectNotFoundException.class);

    }
}
