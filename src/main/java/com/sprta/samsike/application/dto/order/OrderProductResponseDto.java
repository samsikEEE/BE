package com.sprta.samsike.application.dto.order;

import com.sprta.samsike.domain.order.OrderProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderProductResponseDto {
    private UUID productId;
    private String productName;
    private Integer unitPrice;
    private Integer productCnt;

    public OrderProductResponseDto(OrderProduct orderProduct) {
        this.productId = orderProduct.getProduct().getUuid();
        this.productName = orderProduct.getProduct().getName();
        this.unitPrice = orderProduct.getUnitPrice();
        this.productCnt = orderProduct.getProductCnt();
    }
}