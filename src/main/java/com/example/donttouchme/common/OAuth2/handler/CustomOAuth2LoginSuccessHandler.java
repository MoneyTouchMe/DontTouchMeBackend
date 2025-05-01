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

        // 쿠키 생성 대신 Set-Cookie 헤더를 직접 설정
        addCookieHeader(response, "refresh", refreshToken, refreshExpireTime);
        addCookieHeader(response, "accessToken", accessToken, accessExpireTime);

        response.sendRedirect("https://dontouchme.vercel.app");
    }

    // 쿠키 헤더를 직접 추가하는 메소드
    private void addCookieHeader(
            HttpServletResponse response,
            String name,
            String value,
            int maxAge
    ) {
        String cookieValue = String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=None; Secure",
                name, value, maxAge);
        response.addHeader("Set-Cookie", cookieValue);
    }

    // 원래 createCookie 메소드는 사용하지 않으므로 삭제하거나, 아래와 같이 남겨둘 수 있습니다
    private Cookie createCookie(
            final String type,
            final String value,
            final int expireTime
    ) {
        Cookie cookie = new Cookie(type, value);
        cookie.setMaxAge(expireTime);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS에서만 쿠키 전송
        // cookie.setAttribute("SameSite", "None"); - 이 방식은 제대로 작동하지 않음

        return cookie;
    }
}

