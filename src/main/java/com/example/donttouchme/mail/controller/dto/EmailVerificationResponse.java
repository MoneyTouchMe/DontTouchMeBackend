package com.example.donttouchme.mail.controller.dto;

public record EmailVerificationResponse(
        String email,
        String message
) {
}
