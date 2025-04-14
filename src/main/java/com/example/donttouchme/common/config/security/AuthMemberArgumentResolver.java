package com.example.donttouchme.common.config.security;

import com.example.donttouchme.common.jwt.JwtUtil;
import com.example.donttouchme.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        AuthMember authMemberAnnotation = parameter.getParameterAnnotation(AuthMember.class);

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        // 인증이 필요하지 않은 경우 null 반환
        if (!authMemberAnnotation.required()) {
            return null;
        }

        // 토큰이 없거나 Bearer 형식이 아닌 경우
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("인증 토큰이 없거나 형식이 잘못되었습니다.");
        }

        // Bearer 접두사 제거
        String token = authHeader.substring(BEARER_PREFIX.length());

        // 토큰 만료 확인
        if (jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        }

        // access 토큰인지 확인
        if (!"access".equals(jwtUtil.getCategory(token))) {
            throw new IllegalArgumentException("유효한 엑세스 토큰이 아닙니다.");
        }

        // 토큰에서 memberId 추출
        String memberId = jwtUtil.getMemberId(token);

        // Member 엔티티 조회
        return memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}

