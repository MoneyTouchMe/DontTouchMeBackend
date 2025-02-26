package com.example.donttouchme.mail.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailVerificationCodeRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        String email
) {
}
