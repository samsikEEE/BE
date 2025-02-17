package com.sprta.samsike.domain.member;

import com.sprta.samsike.domain.Stamped;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(catalog = "samsike",name = "p_tokens")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Tokens extends Stamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // JWT 액세스 토큰 문자열
    @Column(nullable = false, unique = true, length = 500)
    private String tokenValue;

    // 리프레시 토큰 문자열
    @Column(nullable = false, unique = true, length = 500)
    private String refreshToken;

    // 액세스 토큰 만료 시간
    @Column(nullable = false)
    private LocalDateTime accessTokenExpiration;

    // 리프레시 토큰 만료 시간
    @Column(nullable = false)
    private LocalDateTime refreshTokenExpiration;

    // 블랙리스트 여부. true면 사용 불가능(예: 로그아웃)
    @Column(nullable = false)
    @Builder.Default
    private boolean blacklisted = false;

    // 블랙리스트 등록 시간 (옵션)
    private LocalDateTime blacklistedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    public void blacklist() {
        this.blacklisted = true;
        this.blacklistedAt = LocalDateTime.now();
    }
}
