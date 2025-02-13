package com.sprta.samsike.application.dto.category;

import com.sprta.samsike.domain.restaurant.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private UUID uuid;
    private String category;


    public static CategoryResponseDto CategoryToResponseDto(Category category) {
        System.out.println(category.getCategory());
        return new CategoryResponseDto(category.getUuid(), category.getCategory());
    }

}
