package com.sprta.samsike.domain.ai;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.member.Member;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "samsike", name = "p_ai_log")
public class AiLog extends Stamped {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requestText;

    @Column(columnDefinition = "TEXT")
    private String requestMetadata;

    @Column(columnDefinition = "TEXT")
    private String responseText;

    @Column(columnDefinition = "TEXT")
    private String responseMetadata;

    @Column(nullable = false)
    private String modelVersion;

    @Column(nullable = false)
    private String status;

    private String errorMessage;

}
