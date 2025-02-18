package com.example.donttouchme.common.OAuth2.dto;

public record UserDto(
        String role,
        String name,
        String username,
        String email
) {
}
