package com.sprta.samsike.application.dto.ai;

import lombok.Data;

@Data
public class AiLogRequestDto {
    private String requestText; // AI에 보낸 질문
    private String requestMetadata; // 질문과 관련된 추가 설정 정보
    private String responseText; // AI의 답변
    private String responseMetadata; // 응답의 추가 정보
    private String modelVersion; // 사용된 AI 모델 버전
    private String status; // 요청 상태 (예: 성공, 실패)
    private String errorMessage; // 요청 중 에러 메시지 (nullable)
    private String memberUsername; // 요청을 보낸 사용자
}