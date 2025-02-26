package com.example.donttouchme.mail.mock;

import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationCodeResponse;
import com.example.donttouchme.mail.controller.dto.EmailVerificationRequest;
import com.example.donttouchme.mail.controller.dto.EmailVerificationResponse;
import com.example.donttouchme.mail.service.MailService;
import com.example.donttouchme.member.service.MemberQueryService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableAsync
class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private MemberQueryService memberQueryService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private MailService mailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mailService, "CODE_EXPIRATION", 5L);
    }

    @Test
    void sendVerificationEmail_성공적으로_인증메일_전송_요청() throws Exception {
        // Given
        EmailVerificationCodeRequest request = new EmailVerificationCodeRequest("test@example.com");
        when(memberQueryService.checkDuplicateEmail(request.email())).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(templateEngine.process(eq("emailVerification"), any(Context.class))).thenReturn("<html>mocked</html>");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        EmailVerificationCodeResponse response = mailService.sendVerificationEmail(request);

        // Then
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.state()).isEqualTo("인증메일이 전송 요청되었습니다.");
        verify(valueOperations).set(eq(request.email()), anyString(), eq(5L), eq(TimeUnit.MINUTES));
    }

    @Test
    void sendVerificationEmail_중복된_이메일이면_예외_발생() {
        // Given
        EmailVerificationCodeRequest request = new EmailVerificationCodeRequest("test@example.com");
        when(memberQueryService.checkDuplicateEmail(request.email())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mailService.sendVerificationEmail(request);
        });
        assertThat(exception.getMessage()).isEqualTo("이미 가입한 Email 입니다.");
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    void verifyEmailByCode_유효한_코드로_인증_성공() {
        // Given
        EmailVerificationRequest request = new EmailVerificationRequest("test@example.com", "ABC123");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("test@example.com")).thenReturn("ABC123");

        // When
        EmailVerificationResponse response = mailService.verifyEmailByCode(request);

        // Then
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.message()).isEqualTo("인증이 완료 되었습니다.");
        verify(redisTemplate).delete("test@example.com");
    }

    @Test
    void verifyEmailByCode_잘못된_코드로_인증_실패() {
        // Given
        EmailVerificationRequest request = new EmailVerificationRequest("test@example.com", "WRONG");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("test@example.com")).thenReturn("ABC123");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mailService.verifyEmailByCode(request);
        });
        assertThat(exception.getMessage()).isEqualTo("인증에 싪패 했습니다.");
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void verifyEmailByCode_만료된_코드로_인증_실패() {
        // Given
        EmailVerificationRequest request = new EmailVerificationRequest("test@example.com", "ABC123");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("test@example.com")).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mailService.verifyEmailByCode(request);
        });
        assertThat(exception.getMessage()).isEqualTo("인증에 싪패 했습니다.");
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void sendEmailAsync_비동기_메일_전송_성공() throws Exception {
        // Given
        String email = "test@example.com";
        String verificationCode = "ABC123";
        when(templateEngine.process(eq("emailVerification"), any(Context.class))).thenReturn("<html>mocked</html>");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        CompletableFuture<Void> future = mailService.sendEmailAsync(email, verificationCode);

        // Then
        future.get(2, TimeUnit.SECONDS); // 비동기 작업 완료 대기
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendEmailAsync_메일_전송_실패_시_예외_발생() {
        // Given
        String email = "test@example.com";
        String verificationCode = "ABC123";
        when(templateEngine.process(eq("emailVerification"), any(Context.class))).thenReturn("<html>mocked</html>");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        // MessagingException 대신 RuntimeException으로 변경
        doThrow(new RuntimeException("Mail server error")).when(mailSender).send(mimeMessage);

        // When
        CompletableFuture<Void> future = mailService.sendEmailAsync(email, verificationCode);

        // Then
        assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
    }
}