package com.sprta.samsike.application.dto.ai;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor  // 기본 생성자 자동 생성 (필수)
public class AiRequestDto {
    private String requestText; // 사용자가 보낸 질문
    private String requestMetadata; // 질문에 대한 부가 정보 (예: language, type 등)

}