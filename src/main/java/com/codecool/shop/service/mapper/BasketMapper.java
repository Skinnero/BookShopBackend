package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.basket.BasketDto;
import com.codecool.shop.dto.basket.BasketWithProductsDto;
import com.codecool.shop.dto.basket.NewBasketDto;
import com.codecool.shop.repository.entity.Basket;
import com.codecool.shop.repository.entity.projection.BasketProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface BasketMapper {

    @Mapping(target = "id", source = "basketId")
    @Mapping(target = "products", source = "basketProjectionList")
    BasketWithProductsDto toBasketWithProductsDto(UUID basketId, List<BasketProjection> basketProjectionList);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "isComplete", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "customerId", target = "customer.id")
    Basket dtoToBasket(NewBasketDto newBasketDto);

    @Mapping(target = "basketId", source = "basket.id")
    @Mapping(target = "products", source = "basket.products")
    BasketDto toBasketDto(Basket basket);
}
