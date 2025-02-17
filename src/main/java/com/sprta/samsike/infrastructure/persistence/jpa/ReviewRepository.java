package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.restaurant.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
