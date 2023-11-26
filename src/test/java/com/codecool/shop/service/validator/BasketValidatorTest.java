package com.codecool.shop.service.validator;


import com.codecool.shop.repository.BasketRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class BasketValidatorTest {
    @InjectMocks
    BasketValidator validator;
    @Mock
    BasketRepository repository;

    private UUID basketId;

    @BeforeEach
    void setUp() {
        basketId = UUID.randomUUID();
    }

    @Test
    void testValidateEntityById_ShouldReturnObject_WhenExist() {
        // when
        Mockito.when(repository.findById(basketId)).thenReturn(Optional.of(new Basket()));
        Basket basket = validator.validateByEntityId(basketId);

        // then
        assertThat(basket).isNotNull();
    }

    @Test
    void testValidateEntityById_ShouldThrownObjectNotFoundException_WhenNoBasket() {
        // when
        Mockito.when(repository.findById(basketId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> validator.validateByEntityId(basketId))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}