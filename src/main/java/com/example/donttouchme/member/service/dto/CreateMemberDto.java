package com.example.donttouchme.member.service.dto;

import com.example.donttouchme.OAuth2.dto.OAuth2UserInfo;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;

import static com.example.donttouchme.member.domain.value.ROLE.user;

public record CreateMemberDto(
        String name,
        String email,
        ROLE role,
        LoginProvider loginProvider
) {
        public static CreateMemberDto from(OAuth2UserInfo oAuth2UserInfo){
            return new CreateMemberDto(
                    oAuth2UserInfo.getName(),
                    oAuth2UserInfo.getEmail(),
                    user,
                    oAuth2UserInfo.getProvider()
            );
        }
}
