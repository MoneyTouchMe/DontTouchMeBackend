package com.example.donttouchme.common.jwt.service;

import com.example.donttouchme.common.jwt.JwtUtil;
import com.example.donttouchme.common.jwt.entity.RefreshToken;
import com.example.donttouchme.common.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${spring.jwt.refresh.expireTime}")
    private int expireTime;

    public ResponseEntity<?> reissue(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {

        Cookie[] cookies = request.getCookies();
        String refresh = null;
        Optional<Cookie> refreshCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh"))
                .findFirst();

        if (refreshCookie.isPresent()) {
            refresh = refreshCookie.get().getValue();
        } else {
            throw new IllegalArgumentException("Refresh token not found");
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e){
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }


        if(!jwtUtil.getCategory(refresh).equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String memberId = jwtUtil.getMemberId(refresh);
        String role = jwtUtil.getRole(refresh);

        Optional<RefreshToken> byRefreshToken = refreshTokenRepository.findByRefreshToken(refresh);

        if (byRefreshToken.isEmpty()) {
            return new ResponseEntity<>("refresh token not found", HttpStatus.BAD_REQUEST);
        }

        String newAccessToken = jwtUtil.createAccessToken(Long.valueOf(memberId), role);
        String newRefreshToken = jwtUtil.createRefreshToken(Long.valueOf(refresh), role);

        try {
            refreshTokenRepository.save(RefreshToken.builder()
                    .refreshToken(newRefreshToken)
                    .userId(Long.valueOf(memberId))
                    .build()
            );

        }catch (Exception e) {
            throw new IllegalArgumentException("newRefreshToken save failed");
        }

        try {
            refreshTokenRepository.delete(byRefreshToken.get());
        }catch (Exception e) {
            throw new IllegalArgumentException("refresh token delete failed");
        }

        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie(newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }
    private Cookie createCookie(final String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(expireTime);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
