package com.sprta.samsike.domain.region;

import com.sprta.samsike.domain.Stamped;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

@Entity
@Table(catalog = "samsike", name = "p_sgg_code")
public class SggCode extends Stamped {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.BIGINT)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @Column(unique = true, length = 50, nullable = false)
    private String sggCd;

    @Column(nullable = false)
    private String sggCdNm;
}
