package com.sprta.samsike.infrastructure.persistence.jpa;
import com.sprta.samsike.domain.ai.AiLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiRepository extends JpaRepository<AiLog, UUID> {
    // 추가적인 커스텀 메서드가 필요하면 여기에 작성 가능합니다.
}