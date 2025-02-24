package com.sprta.samsike.application.service;

import ch.qos.logback.core.util.StringUtil;
import com.sprta.samsike.application.dto.category.CategoryResponseDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.restaurant.Category;
import com.sprta.samsike.domain.persistence.jpa.CategoryRepository;
import com.sprta.samsike.domain.persistence.jpa.RestaurantRepository;
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
    private final RestaurantRepository restaurantRepository;


    @Transactional(readOnly = true)
    public ApiResponseDTO<?> getCategory(String category) {
        List<CategoryResponseDto> resultList;

        if(StringUtil.isNullOrEmpty(category)) {
            resultList = categoryRepository.findAllByDeletedAtIsNull().stream().map(CategoryResponseDto::CategoryToResponseDto).toList();
        }else{
            resultList = categoryRepository.findAllByCategoryContainingAndDeletedAtIsNull(category).stream().map(CategoryResponseDto::CategoryToResponseDto).toList();
        }

        return new ApiResponseDTO<>(HttpStatus.OK.toString(), resultList);
    }


    @Transactional
    public void createCategory(String category) {
        if(categoryRepository.existsByCategory(category)) {
            throw new CustomException(ErrorCode.CATE001, "이미 생성된 카테고리 입니다.");
        }
        categoryRepository.save(new Category(category));
    }

    @Transactional(readOnly = true)
    public Category findCategoryById(String categoryId) {
        return categoryRepository.findById(UUID.fromString(categoryId)).orElseThrow(()->
                new CustomException(ErrorCode.CATE002,"일치하는 카테고리가 없습니다.")
        );
    }

    @Transactional
    public void deleteCategory(String categoryId, Member member) {
        Category category = findCategoryById(categoryId);
        // 카테고리 사용 여부 확인
        if(restaurantRepository.existsRestaurantByCategory_Uuid(UUID.fromString(categoryId))) {
            throw new CustomException(ErrorCode.CATE003,"사용 중인 카테고리는 삭제할 수 없습니다.");
        };

        category.delete(member.getUsername());
        categoryRepository.save(category);
    }
}
