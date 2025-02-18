package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.domain.order.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
    List<OrderProduct> findByOrder(Order order);
}
