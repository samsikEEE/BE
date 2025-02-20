package com.sprta.samsike.application.service;

import com.sprta.samsike.domain.ai.AiLog;
import com.sprta.samsike.application.dto.ai.AiLogDto;
import com.sprta.samsike.application.dto.ai.AiRequestDto;
import com.sprta.samsike.application.dto.ai.AiResponseDto;
import com.sprta.samsike.infrastructure.persistence.jpa.AiRepository;
import com.sprta.samsike.domain.member.Member;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AiService {

    private final AiRepository aiRepository;

    public AiService(AiRepository aiRepository) {
        this.aiRepository = aiRepository;
    }

    /**
     * AI 요청 처리 및 결과 저장 (ROLE_OWNER 전용)
     */
    @Transactional
    public AiResponseDto processAiRequest(AiRequestDto aiRequestDto, Member member, String modelVersion) {
        // 권한 검사
        validateRole(member, "ROLE_OWNER");

        // 요청 텍스트 포맷팅
        String formattedRequestText = formatRequestText(aiRequestDto.getRequestText());

        // AI 호출
        String responseText = callAiApi(formattedRequestText, aiRequestDto.getRequestMetadata());

        // 로그 생성 및 저장
        AiLog aiLog = new AiLog(member, formattedRequestText, modelVersion);
        aiLog.updateResponse(responseText, "{}", true, null); // 응답 설정
        aiRepository.save(aiLog);

        // 응답 DTO 반환
        return new AiResponseDto(responseText, "{}", modelVersion, "success", null);
    }

    /**
     * 단일 AI 로그 조회
     */
    @Transactional(readOnly = true)
    public AiLogDto getAiLog(UUID uuid, Member member) {
        validateRole(member, "ROLE_OWNER"); // 권한 검사
        AiLog aiLog = findAiLogById(uuid); // 로그 검색
        return convertEntityToDto(aiLog); // DTO 변환
    }

    /**
     * 특정 AI 로그(Object) 조회 (Entity 직접 반환)
     */
    @Transactional(readOnly = true)
    public AiLog getRawAiLog(UUID uuid, Member member) {
        validateRole(member, "ROLE_OWNER"); // 권한 검사
        return findAiLogById(uuid);
    }

    /**
     * 모든 로그 조회 (List<DTO> 반환)
     */
    @Transactional(readOnly = true)
    public List<AiLogDto> getAllLogs(Member member) {
        validateRole(member, "ROLE_OWNER"); // 권한 검사

        // Entity 리스트 -> DTO 리스트로 변환
        return aiRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * AI API 호출 Mock 메서드 (추후 실제 API 통신으로 교체)
     */
    private String callAiApi(String requestText, String requestMetadata) {
        // 테스트용 Mock 결과 반환
        return "AI 응답: " + requestText.substring(0, Math.min(requestText.length(), 50));
    }

    /**
     * 공통 메서드: 권한 검사
     */
    private void validateRole(Member member, String requiredRole) {
        if (!requiredRole.equals(member.getRole())) {
            throw new AccessDeniedException("이 기능을 사용할 수 있는 권한이 없습니다.");
        }
    }

    /**
     * 요청 텍스트 포맷팅
     */
    private String formatRequestText(String requestText) {
        return requestText + " 답변을 최대한 간결하게 50자 이하로";
    }

    /**
     * UUID로 AI 로그 검색
     */
    private AiLog findAiLogById(UUID uuid) {
        return aiRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Log not found for UUID: " + uuid));
    }

    /**
     * Entity → DTO 변환 로직
     */
    private AiLogDto convertEntityToDto(AiLog aiLog) {
        if (aiLog == null) {
            return new AiLogDto(); // Null 엔티티 처리
        }

        return new AiLogDto(
                aiLog.getUuid(),
                aiLog.getRequestText(),
                aiLog.getRequestMetadata(),
                aiLog.getResponseText(),
                aiLog.getResponseMetadata(),
                aiLog.getModelVersion(),
                aiLog.getStatus(),
                aiLog.getErrorMessage(),
                aiLog.getMember() != null ? aiLog.getMember().getUsername() : null
        );
    }
}