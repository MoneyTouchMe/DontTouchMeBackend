package com.example.donttouchme.common.oauth2.service;

import com.example.donttouchme.common.oauth2.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2UserInfo = {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        //role은 임시적으로 임의 값 대입
        UserDto userDto = new UserDto("ROLE_USER", oAuth2Response.getName(), username);

        return new CustomOAuth2User(userDto);

    }
}
