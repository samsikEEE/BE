package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.product.Product;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByRestaurant(Restaurant restaurant);

    // 레스토랑 UUID로 정렬된 데이터 조회
    List<Product> findByRestaurantUuid(UUID restaurantUuid, Sort sort);

    // 삭제되지 않은 상품만 조회
    List<Product> findAllByRestaurantAndDeletedByIsNull(Restaurant restaurant);

    // 생성일 기준 정렬
    List<Product> findAllByCreatedAtNotNull(Sort sort);

    // 수정일 기준 정렬
    List<Product> findAllByUpdatedAtNotNull(Sort sort);

    boolean existsByName(@NotBlank(message = "제품 이름은 필수입니다.") String name);
    List<Product> findAllByRestaurant_UuidAndDeletedAtIsNull(UUID restaurantId);
}
