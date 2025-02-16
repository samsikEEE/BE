package com.sprta.samsike.domain.region;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(catalog = "samsike", name = "p_restaurant_region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantRegion extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "sgg_cd", nullable = false)
    private SggCode sggCode;

    @Column(nullable = false)
    private String address;


    @Builder
    public RestaurantRegion(Restaurant restaurant, SggCode sggCode, String address) {
        this.restaurant = restaurant;
        this.sggCode = sggCode;
        this.address = address;
    }
}
