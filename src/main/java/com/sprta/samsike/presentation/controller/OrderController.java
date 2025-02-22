package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.order.OrderDetailsResponseDto;
import com.sprta.samsike.application.dto.order.OrderFtfRequestDto;
import com.sprta.samsike.application.dto.order.OrderRequestDto;
import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.application.dto.request.RequestListDTO;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.service.OrderService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j(topic = "주문")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 전체 조회
    @GetMapping
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "주문 조회", description = "사용자의 주문 목록을 조회합니다.")
    public ResponseEntity<ApiResponseDTO<Page<OrderResponseDto>>> getOrders(
            @ParameterObject @ModelAttribute RequestListDTO requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<OrderResponseDto> orders = orderService.getOrders(userDetails.getMember(), requestDto);

        return ResponseEntity.ok(new ApiResponseDTO<>("sucess", orders));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_OWNER')")
    @Operation(summary = "주문 검색", description = "가게 이름 또는 메뉴 이름으로 주문을 검색합니다.")
    public ResponseEntity<ApiResponseDTO<Page<OrderResponseDto>>> searchOrders(
            @RequestParam(name = "restaurantName", required = false) String restaurantName,
            @RequestParam(name = "menuName", required = false) String menuName,
            @ParameterObject @ModelAttribute RequestListDTO requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<OrderResponseDto> orders = orderService.searchOrders(restaurantName, menuName, requestDto.getPageable());

        return ResponseEntity.ok(new ApiResponseDTO<>("success", orders));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MASTER') or hasRole('ROLE_MANAGER')")
    @Operation(summary = "관리자 주문 조회", description = "관리자가 가게의 주문 목록을 조회합니다.")
    public ResponseEntity<ApiResponseDTO<Page<OrderResponseDto>>> getRestaurantOrders(
            @PathVariable("restaurantId") UUID restaurantId,
            @ParameterObject @ModelAttribute RequestListDTO requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<OrderResponseDto> orders = orderService.getRestaurantOrders(userDetails.getMember(), restaurantId,requestDto);

        return ResponseEntity.ok(new ApiResponseDTO<>("sucess", orders));
    }

    @GetMapping("/restaurant/{restaurantId}/search")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MASTER')")
    @Operation(summary = "관리자 주문 검색", description = "관리자가 특정 가게의 주문을 검색합니다.")
    public ResponseEntity<ApiResponseDTO<Page<OrderResponseDto>>> searchOrdersForAdmin(
            @PathVariable("restaurantId") UUID restaurantId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "minAmount", required = false) Integer minAmount,
            @RequestParam(name = "maxAmount", required = false) Integer maxAmount,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "menuName", required = false) String menuName,
            @ParameterObject @ModelAttribute RequestListDTO requestDto) {

        Page<OrderResponseDto> orders = orderService.searchOrdersForAdmin(
                restaurantId, status, minAmount, maxAmount, username, menuName, requestDto.getPageable());

        return ResponseEntity.ok(new ApiResponseDTO<>("success", orders));
    }


    // 상세 주문 조회
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_OWNER') or hasRole('ROLE_MASTER') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<ApiResponseDTO<OrderDetailsResponseDto>> getOrderDetail(
            @PathVariable("orderId") UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        OrderDetailsResponseDto orderDetail = orderService.getOrderDetail(orderId, userDetails.getMember());
        return ResponseEntity.ok(new ApiResponseDTO<>("success", orderDetail));
    }

    // 주문 생성
    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    public ResponseEntity<ApiResponseDTO<String>> createOrder(
            @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.createOrder(requestDto, userDetails.getMember());

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "주문이 완료되었습니다."));
    }

    @PostMapping("/ownerOrder")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @Operation(summary = "대면 주문 생성", description = "대면 주문을 생성합니다.")
    public ResponseEntity<ApiResponseDTO<String>> createFtfOrder(
            @RequestBody OrderFtfRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.createFtfOrder(requestDto, userDetails.getMember());

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "주문이 완료되었습니다."));
    }

    // 주문 접수
    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @Operation(summary = "주문 상태 수정 : 주문 접수", description = "주문을 접수합니다.")
    public ResponseEntity<ApiResponseDTO<String>> acceptOrder(
            @PathVariable("orderId") UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.acceptOrder(orderId, userDetails.getMember());

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "주문 상태 수정이 완료되었습니다."));
    }


    // 주문 취소
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
    public ResponseEntity<ApiResponseDTO<String>> cancelOrder(
            @PathVariable("orderId") UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.cancelOrder(orderId, userDetails.getMember());

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "주문이 취소되었습니다."));
    }

}
