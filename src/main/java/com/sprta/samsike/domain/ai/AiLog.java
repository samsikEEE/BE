package com.sprta.samsike.domain.ai;

import com.sprta.samsike.domain.Stamped;
import com.sprta.samsike.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
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

    // 요청/응답 성공 여부 관리 (기본값: false)
    @Column(nullable = false)
    private boolean isSuccess = false;

    // 기본 생성자
    public AiLog() {}

    // 필수 필드 초기화 생성자
    public AiLog(Member member, String requestText, String modelVersion) {
        this.member = member;
        this.requestText = requestText;
        this.modelVersion = modelVersion;
        this.status = "PENDING"; // 기본 상태 설정
    }

    // 업데이트 메서드
    public void updateResponse(String responseText, String responseMetadata, boolean isSuccess, String errorMessage) {
        this.responseText = responseText;
        this.responseMetadata = responseMetadata;
        this.isSuccess = isSuccess;
        this.status = isSuccess ? "SUCCESS" : "FAILED";
        this.errorMessage = errorMessage;
    }

    // 요청 메타데이터 업데이트
    public void updateRequestMetadata(String requestMetadata) {
        this.requestMetadata = requestMetadata;
    }

}
