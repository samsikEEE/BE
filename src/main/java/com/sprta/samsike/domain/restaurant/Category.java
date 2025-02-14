package com.sprta.samsike.domain.restaurant;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(catalog = "samsike", name = "p_category")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;



    @Builder
    public  Category(String category, String userName) {
        this.category = category;
        this.createdBy = userName;
    }


}

