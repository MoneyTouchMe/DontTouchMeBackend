package com.example.donttouchme.common.OAuth2.service;

import com.example.donttouchme.common.OAuth2.dto.*;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.service.MemberCommandService;
import com.example.donttouchme.member.service.MemberQueryService;
import com.example.donttouchme.member.service.dto.CreateMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            log.info("OAuth2 로그인 요청 시작: {}", userRequest.getClientRegistration().getRegistrationId());
            OAuth2User oAuth2User = super.loadUser(userRequest);
            log.info("OAuth2User 로드 완료: {}", oAuth2User.getAttributes());

            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            log.info("로그인 제공자: {}", registrationId);

            OAuth2UserInfo oAuth2UserInfo = switch (registrationId) {
                case "naver" -> new NaverOAuthInfo(oAuth2User.getAttributes());
                case "google" -> new GoogleOAuthInfo(oAuth2User.getAttributes());
                default -> throw new IllegalArgumentException("잘못된 LoginProvider 입니다.");
            };

            log.info("OAuth2UserInfo 생성 완료: email={}, name={}, provider={}", 
                     oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName(), oAuth2UserInfo.getProvider());

            log.info("기존 회원 조회 시작: email={}, provider={}", 
                     oAuth2UserInfo.getEmail(), oAuth2UserInfo.getProvider());
            
            Member member = memberQueryService.findMemberByEmailAndProvider(
                    oAuth2UserInfo.getEmail(),
                    oAuth2UserInfo.getProvider()
            ).orElseGet(() -> {
                log.info("회원 정보 없음. 새 회원 생성 시작");
                CreateMemberDto createMemberDto = CreateMemberDto.from(oAuth2UserInfo);
                log.info("CreateMemberDto 생성 완료: {}", createMemberDto);
                Member newMember = memberCommandService.createMember(createMemberDto);
                log.info("새 회원 생성 완료: id={}, email={}", newMember.getId(), newMember.getEmail());
                return newMember;
            });

            log.info("CustomUser 생성 시작: memberId={}", member.getId());
            
            OAuth2MemberDto memberDto = OAuth2MemberDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .loginProvider(member.getLoginProvider())
                    .name(member.getName())
                    .role(member.getRole())
                    .build();
                    
            log.info("OAuth2MemberDto 생성 완료: {}", memberDto);
            
            CustomUser customUser = new CustomUser(memberDto);
            log.info("CustomUser 생성 완료");
            
            return customUser;
        } catch (Exception e) {
            log.error("OAuth2 로그인 처리 중 오류 발생", e);
            throw e;
        }
    }
}
