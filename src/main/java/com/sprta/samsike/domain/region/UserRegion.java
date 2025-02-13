package com.sprta.samsike.domain.region;

import com.sprta.samsike.domain.member.Member;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "samsike", name = "p_user_region")
public class UserRegion {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "sgg_cd", nullable = false)
    private SggCode sggCode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean isDefault;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
}
