package com.sprta.samsike.application.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
    private UUID userRegionId;
    private UUID restaurantId;
    private String type;          // 주문 유형 (온라인 / 대면)
    private String status;        // 주문 상태 (주문 중, 완료 등)
    private Integer amount;       // 주문 총액
    private String requirement;   // 요청 사항
    private Boolean isDisposal;   // 일회용품 여부

    public OrderRequestDto(UUID userRegionId, UUID restaurantId, String type, String status,
                           Integer amount, String requirement, Boolean isDisposal) {
        this.userRegionId = userRegionId;
        this.restaurantId = restaurantId;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.requirement = requirement;
        this.isDisposal = isDisposal;
    }
}
