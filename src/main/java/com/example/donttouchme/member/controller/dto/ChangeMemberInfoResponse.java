package com.example.donttouchme.member.controller.dto;

import com.example.donttouchme.member.domain.Member;

public record ChangeMemberInfoResponse(
        Long id,
        String name,
        String email,
        String contact
) {
    public static ChangeMemberInfoResponse from(Member member) {
        return new ChangeMemberInfoResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getContact()
        );
    }
}
