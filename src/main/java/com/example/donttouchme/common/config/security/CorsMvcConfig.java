package com.example.donttouchme.common.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://dontouchme.vercel.app") // 허용할 출처 추가
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드 명시
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Set-Cookie") // 노출할 헤더
                .allowCredentials(true); // 쿠키/인증 정보 허용
    }
}