package com.example.donttouchme.mail.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record EmailVerificationRequest(
        @NotBlank(message = "이메일은 필수 입니다.")
        String email,

        @NotEmpty(message = "인증코드는 필수 입니다.")
        String verificationCode
) {
}
