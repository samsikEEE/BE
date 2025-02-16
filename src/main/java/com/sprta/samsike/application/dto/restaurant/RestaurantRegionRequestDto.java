package com.sprta.samsike.application.dto.restaurant;

import com.sprta.samsike.domain.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantRegionRequestDto {

    private String uuid;

    private String restaurant_id;

    private String ssgCode;

    private String address;

    private Restaurant restaurant;

}
