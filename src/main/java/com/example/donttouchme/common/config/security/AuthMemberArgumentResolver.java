package com.example.donttouchme.common.config.security;

import com.example.donttouchme.common.OAuth2.dto.CustomUser;
import com.example.donttouchme.common.config.security.dto.AuthMemberDto;
import com.example.donttouchme.member.service.security.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();

        // OAuth2 로그인 처리 (CustomUser)
        if (principal instanceof CustomUser) {
            CustomUser customUser = (CustomUser) principal;
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            return new AuthMemberDto(customUser.getMemberId(), customUser.getEmail(), role);
        }

        // JWT 로그인 처리 (CustomUserDetails)
        else if (principal instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            return new AuthMemberDto(customUserDetails.getUserId(), customUserDetails.getUsername(), role);
        }

        throw new IllegalStateException("지원하지 않는 인증 유형입니다.");
    }
}