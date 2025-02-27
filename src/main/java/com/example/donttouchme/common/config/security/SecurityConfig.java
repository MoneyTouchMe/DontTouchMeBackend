package com.example.donttouchme.common.config.security;

import com.example.donttouchme.common.OAuth2.handler.CustomOAuth2LoginSuccessHandler;
import com.example.donttouchme.common.OAuth2.service.OAuth2UserService;
import com.example.donttouchme.common.jwt.JwtUtil;
import com.example.donttouchme.common.jwt.LoginFilter;
import com.example.donttouchme.common.jwt.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final OAuth2UserService oauth2UserService;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(
                    final HttpServletRequest request,
                    final HttpServletResponse response,
                    final AuthenticationException exception
            ) throws IOException, ServletException {
                throw new IllegalArgumentException("AuthenticationFailed");
            }
        };
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic((AbstractHttpConfigurer::disable))
                .csrf(AbstractHttpConfigurer::disable);

        http
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oauth2UserService)
                        ))
                        .successHandler(customOAuth2LoginSuccessHandler)
                        .failureHandler(authenticationFailureHandler())
                );


        // cors
        http
                .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("access"));

                        return configuration;
                    }
                }));

        // authorization
//        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers(
//                        "/", "/api/v1/member/login/**", "/oauth2/**", "/api/v1/member/sign-up",
//                        "/api/v1/member/logout", "/api/v1/jwt/**", "/api/v1//member/check-email-duplicate",
//                        "/swagger-ui/**", "/v3/api-docs/**"
//                ).permitAll()
//                .requestMatchers("/admin").hasRole("ADMIN")
//                .anyRequest().authenticated());

        http.authorizeHttpRequests((auth) -> auth
                .anyRequest().permitAll()
        );


        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // jwt filter
//        http
//                .addFilterAfter(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        http
//                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);


        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        LoginFilter loginFilter = new LoginFilter(authenticationManager, objectMapper, jwtUtil, refreshTokenRepository);
        loginFilter.setFilterProcessesUrl("/api/v1/member/login");

//        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
