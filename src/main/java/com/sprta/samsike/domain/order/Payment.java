package com.sprta.samsike.domain.order;

import com.sprta.samsike.domain.Stamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(catalog = "samsike", name = "p_payment")
public class Payment extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "order_uuid", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String type;    // 결제 타입 : 카드 고정

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatusEnum status;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String cardNumberMasked;

    public Payment(Order order, String type, String transactionId, String cardNumberMasked) {
        this.transactionId = transactionId;
        this.order = order;
        this.type = type;
        this.status = PaymentStatusEnum.SUCCESS;
        this.amount = order.getAmount();
        this.cardNumberMasked = cardNumberMasked;
    }

}
