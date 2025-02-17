package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.order.*;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final UserRegionRepository userRegionRepository;
    private final RestaurantRepository restaurantRepository;


    @Transactional(readOnly = true)
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

    // 상세 주문 조회
    @Transactional(readOnly = true)
    public OrderDetailsResponseDto getOrderDetail(UUID orderId, Member member) {
        // 주문 조회 (존재하지 않으면 예외 발생)
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER001, "주문을 찾을 수 없습니다."));

        // 주문 조회 권한 확인
        MemberRoleEnum role = MemberRoleEnum.valueOf(member.getRole());

        if (role == MemberRoleEnum.ROLE_CUSTOMER) {
            // 고객은 본인의 주문만 조회
            if (!order.getMember().getUsername().equals(member.getUsername())) {
                throw new CustomException(ErrorCode.ORDER003, "본인의 주문만 조회할 수 있습니다.");
            }
        } else if (role == MemberRoleEnum.ROLE_OWNER) {
            // 사장님은 본인 가게의 주문만 조회
            if (!order.getRestaurant().getMember().getUsername().equals(member.getUsername())) {
                throw new CustomException(ErrorCode.ORDER002, "본인 가게의 주문만 조회할 수 있습니다.");
            }
        }

        // 주문에 포함된 상품을 찾아옴
        List<OrderProduct> orderProducts = orderProductRepository.findByOrder(order);

        // 상품 각각의 정보를 리스트에 담아줌
        List<OrderProductResponseDto> productList = orderProducts.stream()
                .map(OrderProductResponseDto::new)
                .collect(Collectors.toList());

        return new OrderDetailsResponseDto(order, productList);
    }


    @Transactional
    public void createOrder(OrderRequestDto requestDto, Member member) {
        // 1. 사용자 지역 조회
        UserRegion userRegion = userRegionRepository.findByMemberAndIsDefaultTrue(member)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER001, "기본 지역 정보를 찾을 수 없습니다."));

        // 2. 가게 조회
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER002, "해당 가게를 찾을 수 없습니다."));

        // 3. 주문 생성 및 저장
        Order order = new Order(
                member,
                userRegion,
                restaurant,
                "온라인",
                "주문 접수 중",
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

    @Transactional
    public void createFtfOrder(OrderFtfRequestDto requestDto, Member owner) {
        // 1. 가게 조회 (사장님 본인의 가게인지 검증)
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER002, "해당 가게를 찾을 수 없습니다."));

        if (!restaurant.getMember().getUsername().equals(owner.getUsername())) {
            throw new CustomException(ErrorCode.ORDER004, "해당 가게의 사장님만 대면 주문을 받을 수 있습니다.");
        }

        // 2. 주문자 조회
        if (requestDto.getUsername() == null || requestDto.getUsername().isEmpty()) {
            throw new CustomException(ErrorCode.MEMB002, "주문자 정보를 입력해야 합니다.");
        }

        Member member = memberRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMB001, "해당 사용자를 찾을 수 없습니다."));

        // 3. 주문자의 기본 지역 조회 (기본 주소가 없으면 예외 발생)
        UserRegion userRegion = userRegionRepository.findByMemberAndIsDefaultTrue(member)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER008, "주문자의 기본 지역 정보가 없습니다."));

        // 4. 주문 생성 및 저장
        Order order = new Order(
                member,
                userRegion,
                restaurant,
                "대면",
                "주문 접수 완료",
                requestDto.getOrderAmount(),
                requestDto.getRequirement(),
                requestDto.getIsDisposal()
        );
        orderRepository.save(order);

        // 5. 주문 상품 저장
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

    @Transactional
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
        order.setStatus("DELETED");
    }

    @Transactional
    public void acceptOrder(UUID orderId, Member member) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER001, "주문을 찾을 수 없습니다."));

        // 주문한 회원 또는 가게 OWNER인지 확인
        if (!order.getMember().getUsername().equals(member.getUsername()) &&
                !order.getRestaurant().getMember().getUsername().equals(member.getUsername())) {
            throw new CustomException(ErrorCode.ORDER005, "주문을 취소할 권한이 없습니다.");
        }

        String orderStatus = order.getStatus();

        if (orderStatus.equals("주문 접수 전")) {
            order.setStatus("주문 접수 완료");

        } else if (orderStatus.equals("주문 접수 완료")) {
            order.setStatus("배달 중");

        } else if (orderStatus.equals("DELETED")) {
            // 주문의 상태가 취소된 상태인 경우
            throw new CustomException(ErrorCode.ORDER009, "이미 취소된 주문입니다.");
        }

    }
}
