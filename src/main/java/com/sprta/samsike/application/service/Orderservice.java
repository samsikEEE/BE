package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.infrastructure.persistence.jpa.OrderRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Orderservice {

    private final OrderRepository orderRepository;

    public Page<OrderResponseDto> getOrders(Member member, UUID restaurantId, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        MemberRoleEnum memberRoleEnum = MemberRoleEnum.valueOf(member.getRole());

        Page<Order> orderList;

        if (memberRoleEnum == MemberRoleEnum.MASTER || memberRoleEnum == MemberRoleEnum.MANAGER) {
            // MASTER, MANAGER는 특정 가게의 주문 조회 가능 (restaurantId 필요)
            if (restaurantId == null) {
                throw new IllegalArgumentException("MASTER, MANAGER는 특정 가게의 주문만 조회할 수 있습니다. restaurantId를 제공하세요.");
            }
            orderList = orderRepository.findByRestaurantUuid(restaurantId, pageable);
        } else if (memberRoleEnum == MemberRoleEnum.OWNER) {
            // OWNER는 자신이 관리하는 모든 가게의 주문 조회
            orderList = orderRepository.findByRestaurantMemberUsername(member.getUsername(), pageable);
        } else {
            // CUSTOMER는 자신의 주문 내역만 조회
            orderList = orderRepository.findByMemberUsername(member.getUsername(), pageable);
        }
        return orderList.map(OrderResponseDto::new);
    }

    public void cancelOrder(UUID orderId, Member member) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER001, "주문을 찾을 수 없습니다."));

        // 주문한 회원 또는 가게 OWNER인지 확인
        if (!order.getMember().getUsername().equals(member.getUsername()) &&
                !order.getRestaurant().getMember().getUsername().equals(member.getUsername())) {
            throw new CustomException(ErrorCode.ORDER005, "주문을 취소할 권한이 없습니다.");
        }

        // 주문 취소 가능 시간 체크 (5분 이내)
        if (Duration.between(order.getCreatedAt(), LocalDateTime.now()).toMinutes() > 5) {
            throw new CustomException(ErrorCode.ORDER006, "주문 후 5분이 지나 취소할 수 없습니다.");
        }

        // 주문 상태 변경
        order.setStatus("CANCELED");
    }
}
