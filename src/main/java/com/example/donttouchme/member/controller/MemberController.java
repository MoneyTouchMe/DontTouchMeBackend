package com.example.donttouchme.member.controller;

import com.example.donttouchme.member.controller.dto.CheckDuplicateEmailResponse;
import com.example.donttouchme.member.service.MemberCommandService;
import com.example.donttouchme.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    public ResponseEntity<CheckDuplicateEmailResponse> checkDuplicateEmail(
            @RequestParam final String email
    ) {
        return ResponseEntity.ok(new CheckDuplicateEmailResponse(
                memberQueryService.checkDuplicateEmail(email)
        ));
    }

}
