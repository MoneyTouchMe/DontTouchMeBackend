package com.example.donttouchme.OAuth2.service;

import com.example.donttouchme.OAuth2.dto.CustomOAuth2User;
import com.example.donttouchme.OAuth2.dto.GoogleOAuthInfo;
import com.example.donttouchme.OAuth2.dto.NaverOAuthInfo;
import com.example.donttouchme.OAuth2.dto.OAuth2UserInfo;
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

        if (memberQueryService.existsMemberByEmailAndProvider(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getProvider())) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }
        CreateMemberDto createMemberDto = CreateMemberDto.from(oAuth2UserInfo);
        Member member = memberCommandService.createMember(createMemberDto);

        return new CustomOAuth2User(member);
    }

}
