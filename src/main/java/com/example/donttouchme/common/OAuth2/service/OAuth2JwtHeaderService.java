package com.example.donttouchme.common.OAuth2.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuth2JwtHeaderService {
    @Value("${spring.jwt.refresh.expireTime}")
    private int refreshExpireTime;
    @Value("${spring.jwt.access.expireTime}")
    private int accessExpireTime;

    public String oauth2JwtHeaderSet(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        Cookie[] cookies = request.getCookies();
        String access = null;

        if(cookies == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "bad";
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("access")){
                access = cookie.getValue();
            }
        }

        if(access == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "bad";
        }

        // 클라이언트의 access 토큰 쿠키를 만료
        response.addCookie(expiredCookie());
        response.addHeader("access", access);
        response.setStatus(HttpServletResponse.SC_OK);

        return "success";
    }
    private Cookie expiredCookie() {
        Cookie cookie = new Cookie("access", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
