package com.sprta.samsike.application.dto.product;

import com.sprta.samsike.application.dto.restaurant.RestaurantResponseDto;
import com.sprta.samsike.domain.product.Product;
import com.sprta.samsike.domain.restaurant.Restaurant;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductSearchResponseDto {

    private RestaurantResponseDto restaurant; // 레스토랑 정보
    private List<ProductResponseDto> products; // 상품 목록

    // 생성자: Restaurant와 상품 리스트를 받아서 필드 초기화
    public ProductSearchResponseDto(Restaurant restaurant, List<Product> productList) {

        // 레스토랑 정보를 RestaurantResponseDto로 변환
        this.restaurant = new RestaurantResponseDto(restaurant);

        // 상품 리스트를 ProductResponseDto 리스트로 변환
        this.products = productList.stream()
                .map(ProductResponseDto::new) // Product 객체를 ProductResponseDto로 변환
                .collect(Collectors.toList());
    }
}