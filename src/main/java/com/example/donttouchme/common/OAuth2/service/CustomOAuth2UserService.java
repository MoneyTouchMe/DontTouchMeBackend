package com.example.donttouchme.common.OAuth2.service;

import com.example.donttouchme.common.OAuth2.dto.*;
import com.example.donttouchme.common.user.domain.value.ROLE;
import com.example.donttouchme.common.user.service.UserCommandService;
import com.example.donttouchme.common.user.service.UserQueryService;
import com.example.donttouchme.common.user.service.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            throw new IllegalArgumentException("잘못된 OAuth 제공자");
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();



        userQueryService.findByUsername(username).ifPresentOrElse(
                user -> {
                    throw new IllegalArgumentException("이미 가입한 유저입니다.");
                },
                () ->{
                    userCommandService.createUser(new UserSignUpDto(
                            ROLE.USER,
                            oAuth2Response.getName(),
                            username,
                            oAuth2Response.getEmail()
                    ));
                }
        );

        UserDto userDto = new UserDto(
                username,
                oAuth2Response.getName(),
                "USER",
                oAuth2Response.getEmail()
        );

        return new CustomOAuth2User(userDto);
    }
}
