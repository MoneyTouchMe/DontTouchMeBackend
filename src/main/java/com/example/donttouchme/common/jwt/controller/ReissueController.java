package com.example.donttouchme.common.jwt.controller;

import com.example.donttouchme.common.jwt.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/reissue")
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping
    public ResponseEntity<?> reissue(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return reissueService.reissue(request, response);
    }
}
