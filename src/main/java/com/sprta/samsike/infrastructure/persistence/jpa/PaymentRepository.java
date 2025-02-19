package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.order.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    Page<Payment> findByOrderRestaurantUuid(UUID restaurantId, Pageable pageable);

    Page<Payment> findByOrderRestaurantMemberUsername(String username, Pageable pageable);

    Page<Payment> findByOrderMemberUsername(String username, Pageable pageable);
}
