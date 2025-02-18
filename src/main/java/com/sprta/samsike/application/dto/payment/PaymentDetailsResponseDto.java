package com.sprta.samsike.application.dto.payment;

import com.sprta.samsike.domain.order.Payment;
import com.sprta.samsike.domain.order.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsResponseDto {
    private UUID uuid;
    private String transactionId;
    private UUID orderId;
    private String type;
    private PaymentStatusEnum status;
    private Integer amount;
    private String cardNumberMasked;

    public PaymentDetailsResponseDto(Payment payment) {
        this.uuid = payment.getUuid();
        this.transactionId = payment.getTransactionId();
        this.orderId = payment.getOrder().getUuid();
        this.type = payment.getType();
        this.status = payment.getStatus();
        this.amount = payment.getAmount();
        this.cardNumberMasked = payment.getCardNumberMasked();
    }


}
