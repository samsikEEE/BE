package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.category.CategoryResponseDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.domain.restaurant.Category;
import com.sprta.samsike.infrastructure.persistence.jpa.CategoryRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public ApiResponseDTO<?> getCategory() {
        List<CategoryResponseDto> resultList = categoryRepository.findAll().stream().map(CategoryResponseDto::CategoryToResponseDto).toList();

        return new ApiResponseDTO<>(HttpStatus.OK.toString(), resultList);
    }


    @Transactional
    public void createCategory(String category) {

        if(categoryRepository.existsByCategory(category)) {
            throw new CustomException(ErrorCode.CATE001, "이미 생성된 카테고리 입니다.");
        }

        categoryRepository.save(Category.builder().
                category(category).
                userName("admin").
                build());
    }

    public Category findCategoryById(String categoryId) {
        if (categoryId == null) {
            throw new CustomException(ErrorCode.CATE002, "카테고리 ID는 null일 수 없습니다.");
        }

        return categoryRepository.findById(UUID.fromString(categoryId)).orElseThrow(()->
                new CustomException(ErrorCode.CATE002,"일치하는 카테고리가 없습니다.")
        );
    }

    public boolean isExistCategoryId(String categoryId) {
        if (categoryId == null)  return  false;
        return categoryRepository.existsById(UUID.fromString(categoryId));
    }

}
