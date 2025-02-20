package com.example.donttouchme.member.controller;

import com.example.donttouchme.member.controller.dto.CheckDuplicateEmailResponse;
import com.example.donttouchme.member.controller.dto.MemberSignUpRequest;
import com.example.donttouchme.member.controller.dto.MemberSignUpResponse;
import com.example.donttouchme.member.service.MemberCommandService;
import com.example.donttouchme.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping("/check-email-duplicate")
    public ResponseEntity<CheckDuplicateEmailResponse> checkDuplicateEmail(
            @RequestParam final String email
    ) {
        return ResponseEntity.ok(new CheckDuplicateEmailResponse(
                memberQueryService.checkDuplicateEmail(email)
        ));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<MemberSignUpResponse> signUp (
            @RequestBody @Validated final MemberSignUpRequest request
    ) {
        return ResponseEntity.ok(
                MemberSignUpResponse.from(memberCommandService.signUp(request))
        );
    }

}
