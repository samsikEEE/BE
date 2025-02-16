package com.sprta.samsike.application.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderProductDto {
    private UUID productId; // 상품 ID
    private Integer productCnt; // 수량
}