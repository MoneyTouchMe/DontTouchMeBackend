package com.example.donttouchme.common.jwt;

import com.example.donttouchme.OAuth2.dto.CustomUser;
import com.example.donttouchme.OAuth2.dto.OAuth2MemberDto;
import com.example.donttouchme.member.domain.value.ROLE;
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

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if(bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = bearerToken.substring(7);  // "Bearer " 제거[1]

        try {
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Access-Control-Expose-Headers", "Token-Expired");
            response.setHeader("Token-Expired", "true");
            return;
        }

        if (!jwtUtil.getCategory(accessToken).equals("access")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        CustomUser customUser = new CustomUser(OAuth2MemberDto.builder()
                .id(Long.parseLong(jwtUtil.getMemberId(accessToken)))
                .role(ROLE.valueOf(jwtUtil.getRole(accessToken)))
                .build()
        );

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
