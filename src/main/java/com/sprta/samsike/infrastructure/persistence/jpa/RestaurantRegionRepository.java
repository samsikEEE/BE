package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.region.RestaurantRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRegionRepository extends JpaRepository<RestaurantRegion, UUID> {
}
