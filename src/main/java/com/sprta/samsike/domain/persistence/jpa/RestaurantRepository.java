package com.sprta.samsike.domain.persistence.jpa;

import com.sprta.samsike.domain.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

   boolean existsRestaurantBySggCode_SggCdAndName(String ssgCd, String name);

    boolean existsRestaurantByCategory_Uuid(UUID categoryId);

    Optional<Restaurant> findByUuidAndDeletedAtIsNull(UUID restaurantId);
}
