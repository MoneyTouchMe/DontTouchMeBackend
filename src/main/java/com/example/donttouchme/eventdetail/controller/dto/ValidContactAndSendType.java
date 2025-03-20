package com.example.donttouchme.eventdetail.controller.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ContactValidator.class)
public @interface ValidContactAndSendType {
    String message() default "연락처 또는 이메일 형식이 올바르지 않습니다."; //검증 실패 시 반환할 기본 메세지

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
