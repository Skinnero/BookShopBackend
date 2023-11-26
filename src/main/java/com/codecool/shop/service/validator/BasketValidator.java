package com.codecool.shop.service.validator;

import com.codecool.shop.repository.BasketRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BasketValidator implements Validator<Basket> {
    private final BasketRepository basketRepository;

    @Override
    public Basket validateByEntityId(UUID id) {
        return basketRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Basket.class));
    }
}
