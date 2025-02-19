package com.example.donttouchme.common.jwt.entity;

import com.example.donttouchme.OAuth2.dto.CustomOAuth2User;
import com.example.donttouchme.common.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");

        if(accessToken == null || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            throw new IllegalArgumentException("토큰이 없거나 잘못된 토큰이빈다.");
        }

        try {
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("토큰이 만료 되었습니다.");
        }

        if (!jwtUtil.getCategory(accessToken).equals("access")) {
            throw new IllegalArgumentException("잘못된 category의 토큰 입니다.");
        }

        // username, role 값을 획득
        String username = jwtUtil.getMemberId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        new CustomOAuth2User()

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

    }
}
