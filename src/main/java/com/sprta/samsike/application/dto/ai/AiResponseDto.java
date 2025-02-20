package com.sprta.samsike.application.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 초기화하는 생성자
public class AiResponseDto {
    private String responseText; // AI가 생성한 질문에 대한 답변
    private String responseMetadata; // 답변과 연결된 부가 정보
    private String modelVersion; // AI 모델 버전 (OpenAI GPT-4, 등)
    private String status; // 요청 상태 (성공, 실패 등)
    private String errorMessage; // 요청 처리 중 발생한 에러 메시지 (없을 경우 null)
}