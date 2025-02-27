package com.example.donttouchme.common.OAuth2.service;

import com.example.donttouchme.common.OAuth2.dto.*;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.service.MemberCommandService;
import com.example.donttouchme.member.service.MemberQueryService;
import com.example.donttouchme.member.service.dto.CreateMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = switch (registrationId) {
            case "naver" -> new NaverOAuthInfo(oAuth2User.getAttributes());
            case "google" -> new GoogleOAuthInfo(oAuth2User.getAttributes());
            default -> throw new IllegalArgumentException("잘못된 LoginProvider 입니다.");
        };

        Member member = memberQueryService.findMemberByEmailAndProvider(
                oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getProvider()
        ).orElseGet(() -> memberCommandService.createMember(
                CreateMemberDto.from(oAuth2UserInfo)
        ));


        return new CustomUser(OAuth2MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .loginProvider(member.getLoginProvider())
                .name(member.getName())
                .role(member.getRole())
                .build());
    }

}
