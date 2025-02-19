package com.sprta.samsike.application.dto.ai;

import lombok.Data;

import java.util.UUID;

@Data
public class AiLogDto {
    private UUID uuid;
    private String requestText;
    private String requestMetadata;
    private String responseText;
    private String responseMetadata;
    private String modelVersion;
    private String status;
    private String errorMessage;
    private String memberUsername;

    // 기본 생성자
    public AiLogDto() {}

    // 모든 필드를 초기화하는 생성자
    public AiLogDto(UUID uuid, String requestText, String requestMetadata, String responseText,
                    String responseMetadata, String modelVersion, String status,
                    String errorMessage, String memberUsername) {
        this.uuid = uuid;
        this.requestText = requestText;
        this.requestMetadata = requestMetadata;
        this.responseText = responseText;
        this.responseMetadata = responseMetadata;
        this.modelVersion = modelVersion;
        this.status = status;
        this.errorMessage = errorMessage;
        this.memberUsername = memberUsername;
    }
}