package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.response.ResponseDTO;
import com.sprta.samsike.infrastructure.persistence.jpa.CategoeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final CategoeyRepository categoeyRepository;

    public ResponseDTO getCategory(){
        return new ResponseDTO<>(categoeyRepository.findAll());
    }
}
