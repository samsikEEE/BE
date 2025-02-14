package com.sprta.samsike.application.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class ProfileDTO {

    private final String username;

    private final String name;

    private final String email;

    private final LocalDateTime createDate;


}
