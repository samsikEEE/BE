package com.sprta.samsike.application.service;

import ch.qos.logback.core.util.StringUtil;
import com.sprta.samsike.domain.region.SggCode;
import com.sprta.samsike.infrastructure.persistence.jpa.SggCodeRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SggCodeService {

    private final SggCodeRepository sggCodeRepository;

    public SggCode findBySggCd(String sggCode) {
        if(StringUtil.isNullOrEmpty(sggCode)) return null;

       return sggCodeRepository.findBySggCd(sggCode).orElseThrow(()->
                new CustomException(ErrorCode.SSGC001,"일치하는 지역코드가 없습니다.")
        );
    }


}
