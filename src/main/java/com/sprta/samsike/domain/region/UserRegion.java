package com.sprta.samsike.domain.region;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(catalog = "samsike", name = "p_user_region")
public class UserRegion extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
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
}
