package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.product.Product;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByRestaurant(Restaurant restaurant);

    // 삭제되지 않은 상품만 조회
    List<Product> findAllByRestaurantAndDeletedByIsNull(Restaurant restaurant);

    boolean existsByName(@NotBlank(message = "제품 이름은 필수입니다.") String name);
    List<Product> findAllByRestaurant_UuidAndDeletedAtIsNull(UUID restaurantId);
}
