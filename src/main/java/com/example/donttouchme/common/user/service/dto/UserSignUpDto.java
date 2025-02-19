package com.example.donttouchme.common.user.service.dto;

import com.example.donttouchme.common.user.domain.value.ROLE;

public record UserSignUpDto(
        ROLE role,
        String name,
        String username,
        String email
) {
}
