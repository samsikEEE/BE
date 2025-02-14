package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.infrastructure.persistence.jpa.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final CategoryRepository categoryRepository;


    public ApiResponseDTO getCategory() {
        return new ApiResponseDTO<>("success", categoryRepository.findAll());
    }
}
