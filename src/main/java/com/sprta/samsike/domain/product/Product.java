package com.sprta.samsike.domain.product;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(catalog = "samsike", name = "p_product")
public class Product extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String name;

    private String imageUrl;
    private String description;

    @Column(nullable = false)
    private Integer price;
}

