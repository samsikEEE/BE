package com.sprta.samsike.domain.restaurant;

import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.region.SggCode;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(catalog = "samsike", name = "p_restaurant")
public class Restaurant {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
}
