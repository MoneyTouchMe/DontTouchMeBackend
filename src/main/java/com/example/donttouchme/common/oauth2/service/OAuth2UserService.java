package com.example.donttouchme.common.oauth2.service;

import com.example.donttouchme.common.oauth2.dto.*;
import com.example.donttouchme.common.oauth2.entity.UserEntity;
import com.example.donttouchme.common.oauth2.enums.Role;
import com.example.donttouchme.common.oauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.example.donttouchme.common.oauth2.enums.Provider.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2UserInfo = {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals(naver.toString())) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals(google.toString())) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);

        //DB에 해당 유저 정보가 있는지 확인 
        if (existData == null) {
            //DB에 유저 정보 저장
            UserEntity userEntity = new UserEntity(
                    username,
                    oAuth2Response.getName(),
                    oAuth2Response.getEmail(),
                    Role.USER.toString()
            );
            userRepository.save(userEntity);

            UserDto userDto = new UserDto(
                    Role.USER,
                    oAuth2Response.getName(),
                    username
            );

            return new CustomOAuth2User(userDto);
        } else {
            existData.setName(oAuth2Response.getName());
            existData.setEmail(oAuth2Response.getEmail());

            userRepository.save(existData);

            UserDto userDto = new UserDto(
                    Role.stringToRole(existData.getRole()),
                    oAuth2Response.getName(),
                    existData.getUsername()
            );

            return new CustomOAuth2User(userDto);
        }


    }
}
