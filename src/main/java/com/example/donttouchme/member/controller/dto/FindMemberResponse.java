package com.example.donttouchme.member.controller.dto;

import com.example.donttouchme.member.domain.Member;

public record FindMemberResponse(
        String name,
        String email,
        String contact
) {
    public static FindMemberResponse from(Member member) {
        return new FindMemberResponse(member.getName(), member.getEmail(), member.getContact());
    }
}
