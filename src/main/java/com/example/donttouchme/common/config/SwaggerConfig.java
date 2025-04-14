package com.example.donttouchme.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "돈 Touch Me API",
                description = "API 명세서",
                version = "V1"
        )
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-definition")
                .pathsToMatch("/api/**")
                .packagesToScan("com.example.donttouchme")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        // 로그인 API 명세 생성
        Operation loginOperation = createLoginOperation();
        PathItem loginPathItem = new PathItem().post(loginOperation);

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", createJWTSecurityScheme())
                        .addSecuritySchemes("oauth2", createOAuth2SecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"))
                // 로그인 API 경로 추가
                .path("/api/v1/member/login", loginPathItem);
    }

    private Operation createLoginOperation() {
        // 로그인 API 기본 정보
        Operation operation = new Operation()
                .tags(Collections.singletonList("인증"))
                .summary("로그인")
                .description("이메일과 비밀번호로 로그인합니다")
                .operationId("login");

        // 요청 스키마 생성
        Schema<?> loginSchema = new ObjectSchema()
                .addProperty("email", new StringSchema().description("사용자 이메일").example("test@test.com"))
                .addProperty("password", new StringSchema().description("비밀번호").example("test1234"));

        // 요청 바디 생성
        RequestBody requestBody = new RequestBody()
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(loginSchema)))
                .required(true);
        operation.setRequestBody(requestBody);

        // 응답 설정
        ApiResponse successResponse = new ApiResponse()
                .description("로그인 성공")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(new ObjectSchema()
                                        .addProperty("message", new StringSchema().example("로그인 성공")))));

        ApiResponse errorResponse = new ApiResponse()
                .description("로그인 실패")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(new ObjectSchema()
                                        .addProperty("message", new StringSchema().example("인증 실패")))));

        ApiResponses responses = new ApiResponses()
                .addApiResponse("200", successResponse)
                .addApiResponse("401", errorResponse);

        operation.setResponses(responses);

        return operation;
    }

    private SecurityScheme createJWTSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    private SecurityScheme createOAuth2SecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl("/oauth2/authorization/google")
                                .tokenUrl("/login/oauth2/code/google")
                                .scopes(new Scopes()
                                        .addString("profile", "프로필 정보")
                                        .addString("email", "이메일 정보"))));
    }
}
