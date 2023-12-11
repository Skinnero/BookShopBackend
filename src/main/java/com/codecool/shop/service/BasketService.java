package com.codecool.shop.service;

import com.codecool.shop.dto.basket.BasketDto;
import com.codecool.shop.dto.basket.EditBasketDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.repository.BasketRepository;
import com.codecool.shop.repository.ProductRepository;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.Product;
import com.codecool.shop.service.exception.ObjectAlreadyExistException;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.BasketMapper;
import com.codecool.shop.service.validator.BasketValidator;
import com.codecool.shop.service.validator.CustomerValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final BasketValidator basketValidator;
    private final CustomerValidator customerValidator;
    private final ProductRepository productRepository;

    public BasketDto getBasketByCustomerId(UUID customerId) {
        customerValidator.validateByEntityId(customerId);
        return basketMapper.toBasketDto(basketRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ObjectNotFoundException(Basket.class)));
    }

    public BasketDto getBasketById(UUID id) {
        return basketMapper.toBasketDto(basketRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Basket.class)));
    }

    public List<BasketDto> getAllBasketsWithProductsByCustomerId(UUID customerId) {
        customerValidator.validateByEntityId(customerId);
        return basketRepository.findAllByCustomerId(customerId).stream()
                .map(basketMapper::toBasketDto)
                .toList();
    }

    public void saveNewBasket(NewBasketDto newBasketDto) {
        basketRepository.findByCustomerId(newBasketDto.customerId())
                .ifPresent(c -> {
                    throw new ObjectAlreadyExistException(Basket.class);
                });
        customerValidator.validateByEntityId(newBasketDto.customerId());
        basketRepository.save(basketMapper.dtoToBasket(newBasketDto));
    }

    public void updateBasket(UUID id, EditBasketDto editBasketDto) {
        Basket basket = basketRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Basket.class));

        basket.removeProducts();

        editBasketDto.products().forEach(product -> {
            basket.appendProduct(productRepository.findById(product)
                    .orElseThrow(() -> new ObjectNotFoundException(product, Product.class)));
        });

        basketRepository.save(basket);
    }

    public void deleteBasket(UUID id) {
        basketValidator.validateByEntityId(id);
        basketRepository.deleteById(id);
    }

}
