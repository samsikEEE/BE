package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.service.Orderservice;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final Orderservice orderService;

    // 주문 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<OrderResponseDto>>> getOrders(
            @RequestParam(required = false) UUID restaurantId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<OrderResponseDto> orders = orderService.getOrders(userDetails.getMember(), restaurantId, page - 1, size, sortBy, isAsc);

        return ResponseEntity.ok(new ApiResponseDTO<>("sucess", orders));
    }

    // 주문 생성


    // 주문 취소
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseDTO<String>> cancelOrder(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        orderService.cancelOrder(orderId, userDetails.getMember());

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "주문이 취소되었습니다."));
    }

}
