package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.infrastructure.persistence.jpa.CategoeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final CategoeyRepository categoeyRepository;

    public ApiResponseDTO getCategory(){
        return new ApiResponseDTO<>("success",categoeyRepository.findAll());
    }
}
