package com.example.donttouchme.member.controller.dto;

public record ChangeMemberInfoRequest(
        String name,
        String newPassword,
        String contact
) {
}
