package com.sprta.samsike.application.dto.ai;

import lombok.Data;

@Data
public class AiRequestDto {
    private String requestText; // 사용자가 보낸 질문
    private String requestMetadata; // 질문에 대한 부가 정보 (예: language, type 등)
}