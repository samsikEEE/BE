package com.sprta.samsike.application.dto.restaurant;

import com.sprta.samsike.domain.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantResponseDto {

    String uuid;
    String name;
    String address;
    String imageUrl;
    String categoryId;
    String category;
    String phone;


    public RestaurantResponseDto(Restaurant restaurant) {
        this.uuid = restaurant.getUuid().toString();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.imageUrl = restaurant.getImageUrl();
        this.categoryId=restaurant.getCategory().getUuid().toString();
        this.category=restaurant.getCategory().getCategory();
        this.phone=restaurant.getPhone();
    }
}
