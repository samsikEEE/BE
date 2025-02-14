package com.sprta.samsike.domain.order;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.product.Product;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "samsike", name = "p_order_product")
public class OrderProduct extends Stamped {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "order_uuid", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_uuid", nullable = false)
    private Product product;

    @Column(name = "product_cnt",nullable = false)
    private Integer productCnt;

    @Column(name = "unit_price",nullable = false)
    private Integer unitPrice;

}

