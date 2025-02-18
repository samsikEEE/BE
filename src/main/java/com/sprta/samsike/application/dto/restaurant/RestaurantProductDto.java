package com.sprta.samsike.application.dto.restaurant;

import com.sprta.samsike.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantProductDto {

    private String productId;
    private String productName;
    private int productPrice;
    private String productDescription;
    private String productImage;


    public RestaurantProductDto(Product product) {
        this.productId = product.getUuid().toString();
        this.productName = product.getName();
        this.productPrice = product.getPrice();
        this.productDescription = product.getDescription();
        this.productImage = product.getImageUrl();
    }

}
