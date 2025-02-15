package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.order.OrderProductDto;
import com.sprta.samsike.application.dto.order.OrderRequestDto;
import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.domain.order.OrderProduct;
import com.sprta.samsike.domain.product.Product;
import com.sprta.samsike.domain.region.UserRegion;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.infrastructure.persistence.jpa.*;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Orderservice {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final UserRegionRepository userRegionRepository;
    private final RestaurantRepository restaurantRepository;


    public Page<OrderResponseDto> getOrders(Member member, UUID restaurantId, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        MemberRoleEnum memberRoleEnum = MemberRoleEnum.valueOf(member.getRole());

        Page<Order> orderList;

        if (memberRoleEnum == MemberRoleEnum.ROLE_MASTER || memberRoleEnum == MemberRoleEnum.ROLE_MANAGER) {
            // MASTER, MANAGER는 특정 가게의 주문 조회 가능 (restaurantId 필요)
            if (restaurantId == null) {
                throw new IllegalArgumentException("MASTER, MANAGER는 특정 가게의 주문만 조회할 수 있습니다. restaurantId를 제공하세요.");
            }
            orderList = orderRepository.findByRestaurantUuid(restaurantId, pageable);
        } else if (memberRoleEnum == MemberRoleEnum.ROLE_OWNER) {
            // OWNER는 자신이 관리하는 모든 가게의 주문 조회
            orderList = orderRepository.findByRestaurantMemberUsername(member.getUsername(), pageable);
        } else {
            // CUSTOMER는 자신의 주문 내역만 조회
            orderList = orderRepository.findByMemberUsername(member.getUsername(), pageable);
        }
        return orderList.map(OrderResponseDto::new);
    }

    @Transactional
    public void createOrder(OrderRequestDto requestDto, Member member) {
        // 1. 사용자 지역 조회
        UserRegion userRegion = userRegionRepository.findById(requestDto.getUserRegionId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER001, "해당 지역 정보를 찾을 수 없습니다."));

        // 2. 가게 조회
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER002, "해당 가게를 찾을 수 없습니다."));

        // 3. 주문 생성 및 저장
        Order order = new Order(
                member,
                userRegion,
                restaurant,
                requestDto.getOrderType(),
                requestDto.getOrderStatus(),
                requestDto.getOrderAmount(),
                requestDto.getRequirement(),
                requestDto.getIsDisposal()
        );
        orderRepository.save(order);

        // 4. 주문 상품 저장
        List<OrderProductDto> productDtos = requestDto.getProducts();
        for (OrderProductDto productDto : productDtos) {
            Product product = productRepository.findById(productDto.getProductId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ORDER003, "상품 정보를 찾을 수 없습니다."));

            // 주문 당시의 상품 가격 저장
            OrderProduct orderProduct = new OrderProduct(
                    order,
                    product,
                    productDto.getProductCnt(),
                    product.getPrice() // 현재 상품 가격을 저장
            );
            orderProductRepository.save(orderProduct);
        }
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
