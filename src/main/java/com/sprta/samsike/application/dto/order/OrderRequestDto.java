package com.sprta.samsike.application.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
    private UUID userRegionId;  // 사용자 지역 ID
    private UUID restaurantId;  // 가게 ID
    private String orderType;   // 주문 유형 (온라인, 대면 등)
    private String orderStatus; // 주문 상태 (주문 중, 배송 중 등)
    private Integer orderAmount; // 총 주문 금액
    private List<OrderProductDto> products; // 주문한 상품 목록
    private String requirement; // 요청사항
    private Boolean isDisposal; // 일회용기 사용 여부
}
