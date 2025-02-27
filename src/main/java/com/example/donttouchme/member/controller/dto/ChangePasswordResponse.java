package com.example.donttouchme.member.controller.dto;

public record ChangePasswordResponse(
        String message
) {
        public static ChangePasswordResponse addMessage(final String message) {
            return new ChangePasswordResponse(message);
        }
}
