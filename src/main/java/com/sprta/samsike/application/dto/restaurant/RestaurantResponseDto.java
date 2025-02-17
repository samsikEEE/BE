package com.sprta.samsike.application.dto.restaurant;

import com.sprta.samsike.domain.restaurant.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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


    public RestaurantResponseDto(UUID uuid, String name, String address, String imageUrl, UUID categoryId, String category, String phone) {
        this.uuid = uuid.toString();
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId.toString();
        this.category = category;
        this.phone = phone;
    }
}
