package com.sprta.samsike.application.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDTO {
    @NotBlank
    @Schema(description = "아이디", example = "test2025")
    private String username;

    @NotBlank
    @Schema(example = "Abcd@1234")
    private String password;

    @Schema(example = "tester")
    private String name;

    @Email
    @NotBlank
    @Schema(example = "test@test")
    private String email;

    private String role = "CUSTOMER";

    @Schema(example = "testtesttest")
    private String address;

}
