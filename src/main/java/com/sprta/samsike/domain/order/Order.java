package com.sprta.samsike.domain.order;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.region.UserRegion;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Entity
@Getter
@Table(catalog = "samsike", name = "p_order")
public class Order extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "user_region_uuid", nullable = false)
    private UserRegion userRegion;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer amount;

    private String requirement;

    @Column(nullable = false)
    private Boolean isDisposal;

    public void setStatus(String status) {
        this.status = status;
    }
}
