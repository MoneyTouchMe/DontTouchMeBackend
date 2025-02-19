package com.example.donttouchme.OAuth2.dto;

import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2MemberDto {
    private Long id;
    private String name;
    private String email;
    private ROLE role;
    private LoginProvider loginProvider;
    private String password;

    @Builder
    public OAuth2MemberDto(Long id, String name, String email, ROLE role, LoginProvider loginProvider) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.loginProvider = loginProvider;
    }

    @Builder
    public OAuth2MemberDto(ROLE role, Long id) {
        this.role = role;
        this.id = id;
    }
}
