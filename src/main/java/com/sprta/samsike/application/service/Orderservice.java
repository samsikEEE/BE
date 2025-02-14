package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.infrastructure.persistence.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Orderservice {

    private final OrderRepository orderRepository;

    public Page<OrderResponseDto> getOrders(UserDetailsImpl userDetails, int i, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Order> orderList;
        if (userDetails.hasRole("ROLE_MASTER") && userDetails.hasRole("ROLE_MANAGER")) {
            orderList = orderRepository.findAll(pageable);
        } else if (userDetails.hasRole("ROLE_OWNER")) {
            orderList = orderRepository.findByRestaurantOwnerUsername(userDetails.getUsername(), pageable);
        } else {
            orderList = orderRepository.findByMemberUsername(userDetails.getUsername(), pageable);
        }

        return orderList.map(OrderResponseDto::new);
    }
}
