package com.codecool.shop.service;

import com.codecool.shop.dto.basket.BasketWithProductsDto;
import com.codecool.shop.dto.basket.EditBasketDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.repository.BasketRepository;
import com.codecool.shop.repository.entity.projection.BasketProjection;
import com.codecool.shop.service.mapper.BasketMapper;
import com.codecool.shop.service.validator.BasketValidator;
import com.codecool.shop.service.validator.CustomerValidator;
import com.codecool.shop.service.validator.ProductValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final BasketValidator basketValidator;
    private final CustomerValidator customerValidator;
    private final ProductValidator productValidator;

    public BasketWithProductsDto getBasketWithProductsById(UUID id) {
        basketValidator.validateByEntityId(id);
        return basketMapper.toBasketWithProductsDto(id, basketRepository.findProductsInBasketById(id));
    }

    public List<BasketWithProductsDto> getAllBasketsWithProductsByCustomerId(UUID customerId) {
        customerValidator.validateByEntityId(customerId);
        List<BasketProjection> basketList = basketRepository.findProductsInBasketByCustomerId(customerId);

        Map<UUID, List<BasketProjection>> groupedBasketMap = basketList.stream()
                .collect(Collectors.groupingBy(BasketProjection::getBasketId));

        return groupedBasketMap.entrySet()
                .stream()
                .map(entry -> basketMapper.toBasketWithProductsDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    public void saveNewBasket(NewBasketDto newBasketDto) {
        customerValidator.validateByEntityId(newBasketDto.customerId());
        basketRepository.save(basketMapper.dtoToBasket(newBasketDto));
    }

    public void updateBasket(UUID id, EditBasketDto editBasketDto) {
        basketValidator.validateByEntityId(id);

        editBasketDto.products().forEach(product -> productValidator.validateByEntityId(product.productId()));

        List<UUID> productList = editBasketDto.products()
                .stream()
                .flatMap(product -> Collections.nCopies(product.quantity(), product.productId())
                        .stream()).toList();

        basketRepository.save(basketMapper.dtoToBasket(id, productList));
    }

    public void deleteBasket(UUID id) {
        basketValidator.validateByEntityId(id);
        basketRepository.deleteById(id);
    }

}
