package com.sprta.samsike.application.dto.restaurant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@Getter
public class RestaurantRequestDto {

    @Schema(description = "가게명", example = "삼식이3")
    @NotBlank(message = "가게명은 필수 입력값입니다.")
    String name;

    @Schema(description = "가게 주인명", example = "tester12")
    @NotBlank
    String userName;

    @Schema(description = "지역구 코드", example = "1100000000")
    @NotBlank(message = "가게 지역은 필수 입력값입니다.")
    String sggCode;

    @Schema(example = "apt 100동")
    @NotBlank(message = "가게 상세주소는 필수 입력값입니다.")
    String address;

    @Schema(example = "category_123")
    @NotBlank(message = "가게 카테고리는 필수 입력값입니다.")
    String categoryId;

    String imageUrl;

    @Schema(example = "02-1234-5678")
    @NotBlank(message = "가게 전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다. (예: 02-1234-5678)")
    String phone;

}
