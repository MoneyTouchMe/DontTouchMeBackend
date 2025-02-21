package com.example.donttouchme.common.jwt.controller;

import com.example.donttouchme.common.OAuth2.service.OAuth2JwtHeaderService;
import com.example.donttouchme.common.jwt.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jwt")
@RequiredArgsConstructor
public class JwtController implements JwtControllerSwagger {

    private final ReissueService reissueService;
    private final OAuth2JwtHeaderService oAuth2JwtHeaderService;

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return reissueService.reissue(request, response);
    }

    @Override
    @GetMapping
    public ResponseEntity<String> oauth2JwtHeader(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return ResponseEntity.ok(oAuth2JwtHeaderService.oauth2JwtHeaderSet(request, response));
    }
}
