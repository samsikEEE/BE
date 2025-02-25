package com.sprta.samsike.domain.persistence.jpa;

import com.sprta.samsike.domain.product.Product;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // 삭제되지 않은 상품만 조회
    List<Product> findAllByRestaurantAndDeletedByIsNull(Restaurant restaurant);

    boolean existsByName(@NotBlank(message = "제품 이름은 필수입니다.") String name);

    List<Product> findAllByRestaurant_UuidAndDeletedAtIsNull(UUID restaurantId);

    //페이지네이션
    Page<Product> findAllByRestaurantUuidAndDeletedByIsNull(UUID restaurantUuid, Pageable pageable);

    boolean existsProductByRestaurant_Uuid(UUID restaurantUuid);
}
