package com.sprta.samsike.domain.product;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.restaurant.Restaurant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    // 활성화 여부
    @Column(nullable = false)
    private boolean isVisible = true; // true로 초기화 (기본 활성 상태)

    public Product() {}
    public Product(Restaurant restaurant, String name, String imageUrl, String description, Integer price) {
        this.restaurant = restaurant;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
    }
    public void update(
            @NotBlank(message = "제품 이름은 필수입니다.") String name,
            @NotNull(message = "가격은 필수입니다.")
            @Positive(message = "가격은 0보다 커야 합니다.") Integer price,
            String description,
            String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }
    //상품숨김 메서드
    public void toggleVisibility(boolean newVisibility) {
        this.isVisible = newVisibility;
    }
}


