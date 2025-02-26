package com.example.donttouchme.mail.service;

import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeResponse;
import com.example.donttouchme.mail.controller.dto.EmailVerificationRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationResponse;
import com.example.donttouchme.member.controller.dto.TempPasswordIssueRequest;
import com.example.donttouchme.member.controller.dto.TempPasswordIssueResponse;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.example.donttouchme.member.domain.value.LoginProvider.original;

@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final RedisTemplate<String, String> redisTemplate;

    private final MemberQueryService memberQueryService;

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public EmailVerificationResponse verifyEmailByCode(
            final EmailVerificationRequest request
    ) {
        if (verifyCode(request.email(), request.verificationCode())) {
            return new EmailVerificationResponse(
                    request.email(),
                    "인증이 완료 되었습니다."
            );
        }
        throw new IllegalArgumentException("인증에 싪패 했습니다.");
    }

    public TempPasswordIssueResponse issueTempPassword(
            final TempPasswordIssueRequest request
    ) {
        Member member = memberQueryService.findMemberByEmailAndProvider(request.email(), original)
                .orElseThrow(
                        () -> new IllegalArgumentException("가입되지 않은 이메일입니다.")
                );

        redisTemplate.opsForValue().set(
                "backup:pwd" + member.getId(),
                member.getPassword(),
                5, TimeUnit.MINUTES
        );

        String tempPassword = createTempPassword();

        member.changePassword(bCryptPasswordEncoder.encode(tempPassword));
        memberRepository.save(member);

        sendTempPasswordEmailAsync(request.email(), tempPassword, member.getId());

        return new TempPasswordIssueResponse(
                request.email(),
                "임시 비밀번호가 이메일로 전송 되었습니다."
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

    private void emailDuplicateCheck(final String email) {
        if(memberQueryService.checkDuplicateEmail(email)) {
            throw new IllegalArgumentException("이미 가입한 Email 입니다.");
        }
    }

    private boolean verifyCode(
            final String email,
            final String verificationCode
    ) {
        String storedCode = redisTemplate.opsForValue().get(email);

        if(storedCode == null) {
            return false;
        }

        if(verificationCode.equals(storedCode)) {
            redisTemplate.delete(email);
            return true;
        }else{
            return false;
        }
    }

    private String createTempPassword() {
        return UUID.randomUUID()
                .toString().substring(0, 10)
                .toUpperCase();
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
                throw new IllegalStateException("비동기 메일 전송 실패: " + email, e);
            }
        });
    }

    private String createTempPasswordContext(final String email, final String tempPassword) {
        Context context = new Context();
        context.setVariable("recipientName", email != null ? email : "고객님");
        context.setVariable("temporaryPassword", tempPassword);
        return templateEngine.process("temporaryPassword", context);
    }

    @Async("mailTaskExecutor")
    public CompletableFuture<Void> sendTempPasswordEmailAsync(
            final String email,
            final String tempPassword,
            final Long memberId
    ) {
        return CompletableFuture.runAsync(() -> {
            String context = createTempPasswordContext(email, tempPassword);
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(email);
                helper.setSubject("dontTouchMe 임시 비밀번호");
                helper.setText(context, true);
                helper.setFrom("your-email@gmail.com");

                mailSender.send(message);
            } catch (MessagingException e) {
                rollbackPassword(memberId);
                throw new IllegalStateException("임시 비밀번호 메일 전송 실패: " + email, e);
            }
        });
    }

    @Transactional
    public void rollbackPassword(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다."));
        String originalPassword = redisTemplate.opsForValue().get("backup:pwd:" + memberId);
        if (originalPassword != null) {
            member.changePassword(originalPassword);
            memberRepository.save(member);
            redisTemplate.delete("backup:pwd:" + memberId);
        }
    }

}

