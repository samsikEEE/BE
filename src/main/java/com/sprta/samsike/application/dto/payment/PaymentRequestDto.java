package com.sprta.samsike.application.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentRequestDto {

    private UUID orderId; // 주문 ID
    private String transactionId; // PG사에서 받은 거래 ID
    private String type;   //  결제 방식 (카드)
    private String cardNumberMasked; // 마스킹된 카드 번호

}
