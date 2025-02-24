package com.example.donttouchme.common.jwt;

import com.example.donttouchme.common.jwt.entity.RefreshToken;
import com.example.donttouchme.common.jwt.repository.RefreshTokenRepository;
import com.example.donttouchme.member.controller.dto.LoginRequest;
import com.example.donttouchme.member.service.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    @Value("${spring.jwt.refresh.expireTime}")
    private int refreshExpireTime;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if(request.getContentType() != null && request.getContentType().contains("application/json")) {
            try {
                LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password(),null);

                return authenticationManager.authenticate(authToken);
            }catch (IOException e) {
                throw new IllegalArgumentException("LoginRequest DTO 매핑 실패");
            }

        }
        throw new IllegalArgumentException("잘못된 contentType 입니다.");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("email");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // authorities 처리 부분 수정
        String role = "user"; // 기본 역할 설정
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.isEmpty()) {
            role = authorities.iterator().next().getAuthority();
        }

        String accessToken = jwtUtil.createAccessToken(customUserDetails.getUserId(), role);
        String refreshToken = jwtUtil.createRefreshToken(customUserDetails.getUserId(), role);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .refreshToken(refreshToken)
                        .userId(customUserDetails.getUserId())
                        .build()
        );

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken, refreshExpireTime));
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }

    private Cookie createCookie(
            final String type,
            final String value,
            final int expireTime
    ) {
        Cookie cookie = new Cookie(type, value);
        cookie.setMaxAge(expireTime);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
