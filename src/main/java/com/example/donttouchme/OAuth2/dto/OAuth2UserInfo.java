package com.example.donttouchme.OAuth2.dto;

import com.example.donttouchme.member.domain.value.LoginProvider;

public interface OAuth2UserInfo {
    LoginProvider getProvider();

    String getEmail();

    String getName();
}
