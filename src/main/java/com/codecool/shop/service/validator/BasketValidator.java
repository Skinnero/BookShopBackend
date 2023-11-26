package com.codecool.shop.service.validator;

import com.codecool.shop.repository.BasketRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BasketValidator implements Validator {
    private final BasketRepository basketRepository;

    @Override
    public void validateByEntityId(UUID id) {
        basketRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Basket.class));
    }
}
