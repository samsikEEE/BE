package com.sprta.samsike.application.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ProfileDTO {

    @Schema(example = "tester01")
    private final String username;

    @Schema(example = "tester01")
    private final String name;

    @Schema(example = "test01@test01")
    private final String email;

    @Schema(example = "")
    private final LocalDateTime createDate;


}
