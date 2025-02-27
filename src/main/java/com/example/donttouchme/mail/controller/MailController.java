package com.example.donttouchme.mail.controller;

import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeResponse;
import com.example.donttouchme.mail.controller.dto.EmailVerificationRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationResponse;
import com.example.donttouchme.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController implements MailControllerSwagger{
    private final MailService mailService;

    @Override
    @PostMapping("/send-verification")
    public ResponseEntity<EmailVerificationCodeResponse> sendVerificationEmail(
            @Validated @RequestBody final EmailVerificationCodeRequest request
    ) {
        return ResponseEntity.ok(mailService.sendVerificationEmail(request));
    }

    @Override
    @PostMapping("/verify")
    public ResponseEntity<EmailVerificationResponse> verifyEmailByCode(
            @Validated @RequestBody final EmailVerificationRequest request
    ) {
        return ResponseEntity.ok(mailService.verifyEmailByCode(request));
    }


}
