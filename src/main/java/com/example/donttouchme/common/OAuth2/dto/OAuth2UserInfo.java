package com.example.donttouchme.common.OAuth2.dto;

import com.example.donttouchme.member.domain.value.LoginProvider;

public interface OAuth2UserInfo {
    LoginProvider getProvider();

    String getEmail();

    String getName();
}
