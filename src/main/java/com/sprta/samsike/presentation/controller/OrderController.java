package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.service.Orderservice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final Orderservice orderService;

    // 주문 전체 조회 (페이지네이션 + 정렬)
//    @GetMapping
//    public ResponseEntity<ApiResponseDTO<Page<OrderResponseDto>>> getOrders(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
//            @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc,
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        Page<OrderResponseDto> orders = orderService.getOrders(userDetails, page - 1, size, sortBy, isAsc);
//        return ResponseEntity.ok(ApiResponseDTO.success(orders));
//    }
}
