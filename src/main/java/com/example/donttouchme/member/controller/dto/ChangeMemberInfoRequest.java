package com.example.donttouchme.member.controller.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeMemberInfoRequest(
        @NotNull(message = "이름은 필수입니다.")
        String name,
        @NotNull(message = "패스워드는 필수입니다.")
        String newPassword,
        @NotNull(message = "연락처는 필수입니다.")
        String contact
) {
}
