package com.sprta.samsike.domain.persistence.jpa;

import com.sprta.samsike.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByMemberUsernameAndDeletedByIsNull(String username, Pageable pageable);

    // 특정 가게의 주문 조회 (MASTER, MANAGER가 restaurantId 제공 시 사용)
    Page<Order> findByRestaurantUuidAndDeletedByIsNull(UUID restaurantId, Pageable pageable);

    // 특정 사장(OWNER)이 관리하는 가게들의 주문 조회
    Page<Order> findByRestaurantMemberUsernameAndDeletedByIsNull(String ownerUsername, Pageable pageable);
}
