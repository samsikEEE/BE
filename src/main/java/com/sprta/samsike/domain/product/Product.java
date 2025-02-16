package com.sprta.samsike.domain.product;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
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

