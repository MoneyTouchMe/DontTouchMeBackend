package com.example.donttouchme.mail.controller.dto;

public record EmailVerificationCodeResponse(
        String email,
        String state
) {
}
