package com.example.donttouchme.member.controller;

import com.example.donttouchme.common.config.security.AuthMember;
import com.example.donttouchme.mail.service.MailService;
import com.example.donttouchme.member.controller.dto.*;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.service.MemberCommandService;
import com.example.donttouchme.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController implements MemberControllerSwagger{

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final MailService mailService;

    @Override
    @GetMapping("/check-email-duplicate")
    public ResponseEntity<CheckDuplicateEmailResponse> checkDuplicateEmail(
            @RequestParam final String email
    ) {
        return ResponseEntity.ok(new CheckDuplicateEmailResponse(
                memberQueryService.checkDuplicateEmail(email)
        ));
    }

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<MemberSignUpResponse> signUp (
            @RequestBody @Validated final MemberSignUpRequest request
    ) {
        return ResponseEntity.ok(
                MemberSignUpResponse.from(memberCommandService.signUp(request))
        );
    }

    @Override
    @PostMapping("/issue-temp-password")
    public ResponseEntity<TempPasswordIssueResponse> issueTempPassword(
            final TempPasswordIssueRequest request
    ) {
        return ResponseEntity.ok(
                mailService.issueTempPassword(request)
        );
    }

    @Override
    @PatchMapping("/password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @AuthMember Member member,
            final ChangePasswordRequest request
    ) {
        return ResponseEntity.ok(memberCommandService.changePassword(member, request));
    }
}
