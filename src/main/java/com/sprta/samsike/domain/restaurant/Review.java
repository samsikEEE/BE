package com.sprta.samsike.domain.restaurant;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Entity
@Table(catalog = "samsike", name = "p_review")
public class Review extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name="username",nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "order_uuid", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Integer rating;

    private String comment;
}
