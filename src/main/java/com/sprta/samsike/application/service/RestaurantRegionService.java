package com.sprta.samsike.application.service;

import com.sprta.samsike.domain.region.RestaurantRegion;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.domain.persistence.jpa.RestaurantRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantRegionService {

    private final RestaurantRegionRepository restaurantRegionRepository;

    @Transactional
    public void createByRestaurant(Restaurant restaurant) {
       RestaurantRegion restaurantRegion =  RestaurantRegion.builder()
               .restaurant(restaurant)
               .sggCode(restaurant.getSggCode())
               .address(restaurant.getAddress())
               .build();

        restaurantRegionRepository.save(restaurantRegion);
    }

}
