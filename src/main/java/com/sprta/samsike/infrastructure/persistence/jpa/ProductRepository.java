package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByRestaurant_UuidAndDeletedAtIsNull(UUID restaurantId);
}
