package com.example.donttouchme.member.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberSignUpRequest(
        @NotBlank(message = "이름은 필수 입니다.")
        String name,

        @NotBlank(message = "email은 필수 입니다.")
        @Email(message = "유효한 이메일 주소를 입력하세요.")
        String email,

        @NotBlank(message = "password는 필수 입니다.")
        @Size(min = 8, message = "password는 8자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "contact는 필수 입니다.")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "유효한 연락처를 입력하세요. (예: 010-1234-5678)")
        String contact

) {
}
