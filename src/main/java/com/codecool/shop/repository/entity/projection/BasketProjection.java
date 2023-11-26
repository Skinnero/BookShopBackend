package com.codecool.shop.repository.entity.projection;

import java.util.UUID;

public interface BasketProjection {
    UUID getBasketId();
    UUID getProductId();
    Integer getQuantity();
    void setBasketId(UUID basketId);
    void setProductId(UUID productId);
    void setQuantity(Integer quantity);
}
