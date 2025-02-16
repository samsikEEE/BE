package com.sprta.samsike.application.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderSearchRequestDto {
    private UUID restaurantId;  // 특정 가게 주문 조회용 (MASTER, MANAGER만 사용)
    private int page;
    private int size;   // 기본값 10
    private String sortBy;
    private boolean isAsc;

    public OrderSearchRequestDto(UUID restaurantId, int page, int size, String sortBy, boolean isAsc) {
        this.restaurantId = restaurantId;
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.isAsc = isAsc;
    }
}