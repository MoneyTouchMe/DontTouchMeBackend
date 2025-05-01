package com.example.donttouchme.common.OAuth2.dto;

import com.example.donttouchme.member.domain.value.LoginProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.example.donttouchme.member.domain.value.LoginProvider.naver;

@Slf4j
public class NaverOAuthInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverOAuthInfo(Map<String, Object> attributes) {
        log.info("Naver OAuth attributes: {}", attributes);
        Object responseObj = attributes.get("response");
        if (responseObj == null) {
            log.error("No 'response' field in Naver OAuth attributes");
            throw new IllegalArgumentException("Invalid Naver OAuth response format");
        }
        this.attributes = (Map<String, Object>) responseObj;
        log.info("Naver user info: {}", this.attributes);
    }

    @Override
    public LoginProvider getProvider() {
        return naver;
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");
        if (email == null) {
            log.error("No 'email' field in Naver user info");
            throw new IllegalArgumentException("Email not found in Naver OAuth response");
        }
        return email.toString();
    }

    @Override
    public String getName() {
        Object name = attributes.get("name");
        if (name == null) {
            log.error("No 'name' field in Naver user info");
            throw new IllegalArgumentException("Name not found in Naver OAuth response");
        }
        return name.toString();
    }
}
