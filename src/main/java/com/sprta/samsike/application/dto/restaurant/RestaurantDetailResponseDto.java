package com.sprta.samsike.application.dto.restaurant;

import com.sprta.samsike.domain.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RestaurantDetailResponseDto {

    private String restaurantId;
    private String restaurantName;
    private String sggCode;
    private String restaurantAddress;

    // 리뷰
    private Double ratingAvg;
    private Long reviewCount;

    // 상품 List
    private List<RestaurantProductDto> productList;


    public RestaurantDetailResponseDto(Restaurant restaurant, ReviewCountDto reviewCountDto , List<RestaurantProductDto> productList ) {
        this.restaurantId = restaurant.getUuid().toString();
        this.restaurantName = restaurant.getName();
        this.sggCode = restaurant.getSggCode().getSggCd();
        this.restaurantAddress = restaurant.getAddress();
        this.ratingAvg = reviewCountDto.getRatingAvg();
        this.reviewCount = reviewCountDto.getCount();
        this.productList = productList;
    }
}
