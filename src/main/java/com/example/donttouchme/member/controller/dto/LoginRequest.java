package com.example.donttouchme.member.controller.dto;

public record LoginRequest(
        String email,
        String password
) {
}
