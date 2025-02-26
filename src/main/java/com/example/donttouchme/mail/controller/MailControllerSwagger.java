package com.example.donttouchme.mail.controller;

import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeResponse;
import com.example.donttouchme.mail.controller.dto.EmailVerificationRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "mail 관련 API", description = "mail 관련 API")
public interface MailControllerSwagger {

    @Operation(
            summary = "이메일 인증코드 발송 API",
            description = "이메일 인증 코드를 발송합니다."
    )
    ResponseEntity<EmailVerificationCodeResponse> sendVerificationEmail(
            EmailVerificationCodeRequest emailVerificationCodeRequest
    );

    @Operation(
            summary = "이메일 인증 API",
            description = "발송 받은 코드로 이메일 인증 수행"
    )
    public ResponseEntity<EmailVerificationResponse> verifyEmailByCode(
            EmailVerificationRequest emailVerificationRequest
    );
}
