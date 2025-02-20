package com.example.donttouchme.common.OAuth2.controller;

import com.example.donttouchme.common.OAuth2.service.OAuth2JwtHeaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth2-jwt-header")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2JwtHeaderService jwtHeaderService;

    @PostMapping
    public ResponseEntity<String> oauth2JwtHeader(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return ResponseEntity.ok(jwtHeaderService.oauth2JwtHeaderSet(request, response));

    }
}
