package com.sprta.samsike.domain.region;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

@Entity
@Table(catalog = "samsike", name = "p_sgg_code")
public class SggCode {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.BIGINT)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @Column(unique = true, length = 50, nullable = false)
    private String sggCd;

    @Column(nullable = false)
    private String sggCdNm;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String updatedBy;

    private LocalDateTime deletedAt;
    private String deletedBy;
}
