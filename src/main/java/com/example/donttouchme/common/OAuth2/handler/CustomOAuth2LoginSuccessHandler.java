package com.example.donttouchme.common.OAuth2.handler;

import com.example.donttouchme.common.OAuth2.dto.CustomUser;
import com.example.donttouchme.common.jwt.JwtUtil;
import com.example.donttouchme.common.jwt.entity.RefreshToken;
import com.example.donttouchme.common.jwt.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${spring.jwt.refresh.expireTime}")
    private int refreshExpireTime;
    @Value("${spring.jwt.access.expireTime}")
    private int accessExpireTime;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException, ServletException {

        CustomUser customOAuth2User = (CustomUser) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String refreshToken = jwtUtil.createRefreshToken(
                customOAuth2User.getMemberId(),
                role
        );

        String accessToken = jwtUtil.createAccessToken(
                customOAuth2User.getMemberId(),
                role
        );

        refreshTokenRepository.save(new RefreshToken(refreshToken, customOAuth2User.getMemberId()));

        response.addCookie(createCookie("refresh", refreshToken,refreshExpireTime));
        response.addCookie(createCookie("accessToken", accessToken,accessExpireTime));
        response.sendRedirect("http://localhost:3000");

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
