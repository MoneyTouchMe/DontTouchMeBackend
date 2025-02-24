package com.example.donttouchme.member.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MemberSignUpRequest(
        @NotBlank(message = "이름은 필수 입니다.")
        String name,

        @NotBlank(message = "email은 필수 입니다.")
        String email,

        @NotBlank(message = "password는 필수 입니다.")
        @Min(value = 8, message = "password는 8자 이상입니다.")
        String password
) {
}
