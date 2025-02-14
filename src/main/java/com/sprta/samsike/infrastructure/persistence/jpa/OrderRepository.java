package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {


}
