package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.infrastructure.persistence.jpa.OrderRepository;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
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

    public Page<OrderResponseDto> getOrders(Member member, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        MemberRoleEnum memberRoleEnum = member.getRole();

        Page<Order> orderList;
        if (memberRoleEnum == MemberRoleEnum.MASTER || memberRoleEnum == MemberRoleEnum.MANAGER) {
            orderList = orderRepository.findAll(pageable);
        } else if (memberRoleEnum == MemberRoleEnum.OWNER) {
            orderList = orderRepository.findByMemberUsername(member.getUsername(), pageable);
        } else {
            orderList = orderRepository.findByMemberUsername(member.getUsername(), pageable);
        }

        return orderList.map(OrderResponseDto::new);
    }
}
