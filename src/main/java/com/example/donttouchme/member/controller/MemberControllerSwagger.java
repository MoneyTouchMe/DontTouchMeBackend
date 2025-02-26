package com.example.donttouchme.member.controller;

import com.example.donttouchme.member.controller.dto.*;
import com.example.donttouchme.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "member 관련 API", description = "Member 관련 API")
public interface MemberControllerSwagger {

    @Operation(
            summary = "이메일 중복확인 API",
            description = "이메일 중복확인 API"
    )
    ResponseEntity<CheckDuplicateEmailResponse> checkDuplicateEmail(
            String email
    );

    @Operation(
            summary = "회원가입 API",
            description = "회원가입 API"
    )
     ResponseEntity<MemberSignUpResponse> signUp(
            final MemberSignUpRequest request
    );

    @Operation(
            summary = "임시 비빌번호 발급 API",
            description = "임시 비밀번호를 발급한다."
    )
    ResponseEntity<TempPasswordIssueResponse> issueTempPassword(
            TempPasswordIssueRequest request
    );

    @Operation(
            summary = "비밀 번호 변경 API",
            description = "비밀 번호를 변경합니다."
    )
    ResponseEntity<ChangePasswordResponse> changePassword(
            Member member,
            ChangePasswordRequest request
    );
}
