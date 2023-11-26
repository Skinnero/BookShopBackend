package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.basket.EditBasketDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

public class BasketMapperTest {
    BasketMapper mapper = Mappers.getMapper(BasketMapper.class);

    private Basket basket;
    private NewBasketDto newBasketDto;
    private EditBasketDto editBasketDto;

    @BeforeEach
    void setUp() {
        basket = new Basket(
                UUID.randomUUID(),
                false,
                new Customer(),
                List.of()
        );

        newBasketDto = new NewBasketDto(
                UUID.randomUUID()
        );

        editBasketDto = new EditBasketDto(
                List.of()
        );
    }

    @Test
    void name() {
    }

    //    @Test
//    void testDtoToBasket_ShouldReturnBasket_WhenCalled() {
//        // when
//        Basket expectedBasketOne = mapper.dtoToBasket(basket.getId());
//        Basket expectedBasketTwo = mapper.dtoToBasket(newBasketDto);
//        Basket expectedBasketThree = mapper.dtoToBasket(basket.getId(), newBasketDto);
//
//        // then
//        assertThat(expectedBasketOne.getId()).isEqualTo(basket.getId());
//
//        assertThat(expectedBasketTwo.getCustomer().getId()).isEqualTo(newBasketDto.customerId());
//        assertThat(expectedBasketTwo.getProducts().size()).isEqualTo(newBasketDto.products().size());
//
//        assertThat(expectedBasketThree.getId()).isEqualTo(basket.getId());
//        assertThat(expectedBasketThree.getCustomer().getId()).isEqualTo(newBasketDto.customerId());
//        assertThat(expectedBasketThree.getProducts().size()).isEqualTo(newBasketDto.products().size());
//    }
}
