package com.example.donttouchme.member.controller.dto;

import com.example.donttouchme.member.domain.Member;

public record MemberSignUpResponse(
        Long id,
        String name,
        String email
) {
        public static MemberSignUpResponse from(Member member) {
            return new MemberSignUpResponse(
                    member.getId(),
                    member.getName(),
                    member.getEmail()
            );
        }

}
