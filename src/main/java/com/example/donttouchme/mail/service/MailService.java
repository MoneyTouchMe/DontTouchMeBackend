package com.example.donttouchme.mail.service;

import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeResponse;
import com.example.donttouchme.member.service.MemberQueryService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final RedisTemplate<String, String> redisTemplate;

    private final MemberQueryService memberQueryService;

    @Value("${spring.data.redis.mailExTime}")
    private long CODE_EXPIRATION;

    public EmailVerificationCodeResponse sendVerificationEmail(
            final EmailVerificationCodeRequest request
    ) {
        emailDuplicateCheck(request.email());

        String verificationCode = createVerificationCode();

        redisTemplate.opsForValue().set(request.email() ,verificationCode, CODE_EXPIRATION, TimeUnit.MINUTES);

        sendEmailAsync(request.email(), verificationCode);

        return new EmailVerificationCodeResponse(
                request.email(),
                "인증메일이 전송 요청되었습니다."
        );
    }

    private String createVerificationCode() {
        return UUID.randomUUID()
                .toString().substring(0, 6)
                .toUpperCase();
    }

    private String createContext(
            final String email,
            final String verificationCode
    ) {
        Context context = new Context();
        context.setVariable("recipientName", email != null ? email : "고객님");
        context.setVariable("verificationCode", verificationCode);
        return templateEngine.process("emailVerification", context);
    }


    @Async("mailTaskExecutor") // 특정 스레드 풀 사용 (설정 안 하면 기본 풀 사용)
    public CompletableFuture<Void> sendEmailAsync(
            final String email,
            final String verificationCode
    ) {
        return CompletableFuture.runAsync(() -> {
            String context = createContext(email, verificationCode);
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(email);
                helper.setSubject("dontTouchMe 이메일 인증 코드");
                helper.setText(context, true);
                helper.setFrom("your-email@gmail.com");

                mailSender.send(message);
            } catch (MessagingException e) {
                // 비동기 작업 내 예외 처리 (로깅 추천)
                throw new IllegalStateException("비동기 메일 전송 실패: " + email, e);
            }
        });
    }

    private void emailDuplicateCheck(final String email) {
        if(memberQueryService.checkDuplicateEmail(email)) {
            throw new IllegalArgumentException("이미 가입한 Email 입니다.");
        }
    }

}

