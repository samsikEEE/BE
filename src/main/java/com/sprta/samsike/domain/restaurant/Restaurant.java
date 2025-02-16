package com.sprta.samsike.domain.restaurant;

import ch.qos.logback.core.util.StringUtil;
import com.sprta.samsike.application.dto.restaurant.RestaurantRequestDto;
import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.region.SggCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(catalog = "samsike", name = "p_restaurant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_username", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_uuid", nullable = false)
    private Category category;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "sgg_code", nullable = false)
    private SggCode sggCode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;


    @Builder
    public Restaurant(String name, Member member, Category category, String imageUrl, SggCode sggCode, String address, String phone) {
        this.name = name;
        this.member = member;
        this.category = category;
        this.imageUrl = imageUrl;
        this.sggCode = sggCode;
        this.address = address;
        this.phone = phone;
    }

    public void update(RestaurantRequestDto requestDto , Category category) {
        if(!StringUtil.isNullOrEmpty(requestDto.getAddress())) this.address = requestDto.getAddress();
        if(!StringUtil.isNullOrEmpty(requestDto.getImageUrl())) this.imageUrl = requestDto.getImageUrl();
        if(!StringUtil.isNullOrEmpty(requestDto.getPhone())) this.phone = requestDto.getPhone();
        if(category != null) this.category = category;
    }
}
