package com.sprta.samsike.application.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDTO {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Size(min = 4, max = 10, message = "아이디는 최소 4자 이상, 최대 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 알파벳 소문자(a-z)와 숫자(0-9)만 포함해야 합니다.")
    @Schema(description = "아이디", example = "test2025")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 최대 15자 이하여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @Schema(example = "Abcd@1234")
    private String password;

    @Schema(example = "tester")
    private String name;

    @Email
    @NotBlank
    @Schema(example = "test@test")
    private String email;

    @Schema(example = "ROLE_CUSTOMER")
    private String role;

    @Schema(example = "testtesttest")
    private String address;

}
