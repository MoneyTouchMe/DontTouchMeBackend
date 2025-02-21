package com.example.donttouchme.member.controller;

import com.example.donttouchme.member.controller.dto.CheckDuplicateEmailResponse;
import com.example.donttouchme.member.controller.dto.MemberSignUpRequest;
import com.example.donttouchme.member.controller.dto.MemberSignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "member 관련 API", description = "Member 관련 API")
public interface MemberControllerSwagger {

    @Operation(
            summary = "이메일 중복확인 API",
            description = "이메일 중복확인 API"
    )
    public ResponseEntity<CheckDuplicateEmailResponse> checkDuplicateEmail(
            String email
    );

    @Operation(
            summary = "회원가입 API",
            description = "회원가입 API"
    )
    public ResponseEntity<MemberSignUpResponse> signUp(
            final MemberSignUpRequest request
    );
}
