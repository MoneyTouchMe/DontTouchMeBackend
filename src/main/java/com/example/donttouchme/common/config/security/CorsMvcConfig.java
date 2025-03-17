package com.example.donttouchme.common.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://dontouchme.vercel.app") // 필요한 오리진 추가
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 필요한 메서드 명시
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("access", "Set-Cookie") // 필요한 노출 헤더 추가
                .allowCredentials(true) // 인증 정보 허용
                .maxAge(3600L); // preflight 요청 캐싱 시간 (초 단위)
    }
}