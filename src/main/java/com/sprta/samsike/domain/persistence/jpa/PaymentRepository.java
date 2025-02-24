package com.sprta.samsike.domain.persistence.jpa;

import com.sprta.samsike.domain.order.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    Page<Payment> findByOrderRestaurantUuidAndDeletedByIsNull(UUID restaurantId, Pageable pageable);

    Page<Payment> findByOrderRestaurantMemberUsernameAndDeletedByIsNull(String username, Pageable pageable);

    Page<Payment> findByOrderMemberUsernameAndDeletedByIsNull(String username, Pageable pageable);
}
