package com.sprta.samsike.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprta.samsike.domain.ai.AiLog;
import com.sprta.samsike.application.dto.ai.AiLogDto;
import com.sprta.samsike.application.dto.ai.AiRequestDto;
import com.sprta.samsike.application.dto.ai.AiResponseDto;
import com.sprta.samsike.domain.persistence.jpa.AiRepository;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AiService {

    @Value("${google.ai.api.url}")
    private String aiApiUrl;

    @Value("${google.ai.api.key}")

    private String apiKey;

    private final AiRepository aiRepository;

    public AiService(AiRepository aiRepository) {
        this.aiRepository = aiRepository;
    }

    private String formatRequestText(String originalText) {
        // 원본 질문에 "답변을 최대한 간결하게 50자 이내로" 문구를 추가
        return originalText + " 답변을 최대한 간결하게 50자 이내로";
    }

    /**
     * AI 요청 처리 및 결과 저장 (ROLE_OWNER 전용)
     */
    @Transactional
    public AiResponseDto processAiRequest(AiRequestDto aiRequestDto, Member member, String modelVersion) {
        validateRole(member, "ROLE_OWNER");

        // AI 요청 텍스트 가공
        String formattedText = formatRequestText(aiRequestDto.getRequestText());

        // AI 요청 로그 저장 (PENDING)
        AiLog aiLog = new AiLog(member, formattedText, modelVersion);
        aiLog.updateRequestMetadata(aiRequestDto.getRequestMetadata());
        aiRepository.save(aiLog);

        // AI API 호출
        String responseText = callAiApi(aiLog);
        aiRepository.save(aiLog);

        // 응답 DTO 생성 및 반환
        return new AiResponseDto(
                aiLog.getResponseText(),
                aiLog.getResponseMetadata(),
                modelVersion,
                aiLog.getStatus(),
                aiLog.getErrorMessage()
        );
    }

    /**
     * AI API 호출
     */
    private String callAiApi(AiLog aiLog) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 JSON 생성
        String requestBody = String.format(
                """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """, aiLog.getRequestText()
        );

        // HTTP 헤더 설정 (포스트맨에서 직접 API 키 입력)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String requestUrl = aiApiUrl + "?key=" + apiKey;
        try {
            // API 요청 및 응답 처리
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    requestUrl, HttpMethod.POST, entity, String.class
            );

            // 응답 상태 코드 확인
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("AI API 호출 실패: " + responseEntity.getStatusCode());
            }

            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = responseEntity.getBody();

            if (responseBody == null || responseBody.isEmpty()) {
                throw new RuntimeException("AI API 응답이 비어 있습니다.");
            }

            JsonNode jsonResponse = objectMapper.readTree(responseBody);

            // 응답 데이터 추출
            String responseText = jsonResponse
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            // 응답 저장
            aiLog.updateResponse(responseText, null, true, null);
            return responseText;
        } catch (Exception e) {
            throw new RuntimeException("AI API 호출 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 공통 메서드: 권한 검사 (Optional 활용)
     */
    private void validateRole(Member member, String requiredRole) {
        Optional.ofNullable(member)
                .map(Member::getRole)
                .filter(role -> role.equals(requiredRole))
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH001, "이 기능을 사용할 수 있는 권한이 없습니다."));
    }

    /**
     * UUID로 AI 로그 검색
     */
    private AiLog findAiLogById(UUID uuid) {
        return aiRepository.findById(uuid)
                .orElseThrow(() -> new CustomException(ErrorCode.UUID001 , " 일치하는 고유번호 없음"));
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
     * ✅ AI 로그 목록 조회 (정렬 + 페이지네이션 적용)
     * @param member     - 요청자 정보 (권한 체크)
     * @param sortBy     - 정렬 기준 (createdAt / updatedAt)
     * @param ascending  - 오름차순 여부 (true=오름차순, false=내림차순)
     * @param page       - 조회할 페이지 번호 (0부터 시작)
     * @param size       - 한 페이지당 데이터 개수 (10, 30, 50만 허용)
     * @return           - AI 로그 목록 (페이지네이션 적용)
     */
    @Transactional(readOnly = true)
    public Page<AiLogDto> getAllLogs(Member member, String sortBy, boolean ascending, int page, int size) {
        validateRole(member, "ROLE_OWNER"); // 권한 검사

        // ✅ 허용된 페이지 크기만 적용 (10, 30, 50만 허용, 그 외는 10으로 고정)
        List<Integer> allowedSizes = List.of(10, 30, 50);
        if (!allowedSizes.contains(size)) {
            size = 10;
        }

        // ✅ 정렬 기준 (createdAt 또는 updatedAt만 허용)
        if (!"createdAt".equals(sortBy) && !"updatedAt".equals(sortBy)) {
            sortBy = "createdAt"; // 기본값 설정
        }

        // ✅ 정렬 방향 설정
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // ✅ 페이지 요청 객체 생성
        Pageable pageable = PageRequest.of(page, size, sort);

        // ✅ 데이터베이스 조회 (Page 객체 반환)
        Page<AiLog> aiLogPage = aiRepository.findAll(pageable);

        // ✅ Entity -> DTO 변환 및 반환
        return aiLogPage.map(this::convertEntityToDto);
    }


    /**
     * Entity → DTO 변환 로직
     */
    private AiLogDto convertEntityToDto(AiLog aiLog) {
        if (aiLog == null) {
            return new AiLogDto();
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
