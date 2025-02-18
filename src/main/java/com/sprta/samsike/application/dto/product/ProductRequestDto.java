package com.sprta.samsike.application.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;


    @Getter
    public class ProductRequestDto {

        @NotBlank(message = "제품 이름은 필수입니다.")
        private String name; // Product 이름

        private String restaurantID; //생성 필요한 restaurantId

        private String imageUrl; // Product 이미지 URL (선택 항목)

        private String description; // Product 설명 (선택 항목)

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        private Integer price; // Product 가격
}