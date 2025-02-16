package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
}
