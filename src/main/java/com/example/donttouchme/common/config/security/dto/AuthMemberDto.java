package com.example.donttouchme.common.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthMemberDto {
    private final Long userId;
    private final String email; // 또는 username
    private final String role;
}