package com.example.donttouchme.common.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "JWT 토큰관련 API", description = "JWT 토큰관련 API")
public interface JwtControllerSwagger {

    @Operation(
            summary = "accessToken 재발행 API",
            description = "refreshToken 으로 accessToken 재발행"
    )
    public ResponseEntity<?> reissue(
            HttpServletRequest request,
            HttpServletResponse response
    );

    @Operation(
            summary = "accessToken 쿠키에서 Header로 변환",
            description = "쿠키에 있던 토큰을 Header로 이동시켜줌"
    )
    public ResponseEntity<String> oauth2JwtHeader(
            HttpServletRequest request,
            HttpServletResponse response
    );
}
