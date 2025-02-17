package com.sprta.samsike.domain.order;

import com.sprta.samsike.domain.Stamped;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(catalog = "samsike", name = "p_payment")
public class Payment extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @Column(name = "transaction_id")
    private Long transcationId;

    @ManyToOne
    @JoinColumn(name = "order_uuid", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String Type;

    @Column(nullable = false)
    private String Status;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String cardNumberMasked;

}
