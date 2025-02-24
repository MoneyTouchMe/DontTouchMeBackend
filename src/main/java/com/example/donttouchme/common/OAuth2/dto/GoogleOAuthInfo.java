package com.example.donttouchme.common.OAuth2.dto;

import com.example.donttouchme.member.domain.value.LoginProvider;

import java.util.Map;

import static com.example.donttouchme.member.domain.value.LoginProvider.google;

public class GoogleOAuthInfo implements OAuth2UserInfo{
    private  final Map<String, Object> attributes;

    public GoogleOAuthInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public LoginProvider getProvider() {
        return google;
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
