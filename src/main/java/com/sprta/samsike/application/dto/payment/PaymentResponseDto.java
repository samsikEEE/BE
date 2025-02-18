package com.sprta.samsike.application.dto.payment;

import com.sprta.samsike.domain.order.Payment;
import com.sprta.samsike.domain.order.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private UUID uuid;
    private UUID orderId;
    private String transactionId;
    private Integer amount;
    private String cardNumberMasked;
    private PaymentStatusEnum status;
    private LocalDateTime createdAt;

    public PaymentResponseDto(Payment payment) {
        this.uuid = payment.getUuid();
        this.orderId = payment.getOrder().getUuid();
        this.transactionId = payment.getTransactionId();
        this.amount = payment.getAmount();
        this.cardNumberMasked = payment.getCardNumberMasked();
        this.status = payment.getStatus();
        this.createdAt = payment.getCreatedAt();
    }
}
