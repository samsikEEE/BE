package com.sprta.samsike.application.dto.product;

import com.sprta.samsike.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductResponseDto {

    private UUID ProductUuid;
    private String name;
    private String imageUrl;
    private String description;
    private Integer price;
    private UUID restaurantUuid;

    public ProductResponseDto(Product product) {
        this.ProductUuid = product.getUuid();
        this.name = product.getName();
        this.imageUrl = product.getImageUrl();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.restaurantUuid = product.getRestaurant().getUuid();
    }
}