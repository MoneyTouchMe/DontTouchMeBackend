package com.example.donttouchme.OAuth2.dto;

import com.example.donttouchme.member.domain.value.LoginProvider;

import java.util.Map;

import static com.example.donttouchme.member.domain.value.LoginProvider.naver;

public class NaverOAuthInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverOAuthInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public LoginProvider getProvider() {
        return naver;
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
