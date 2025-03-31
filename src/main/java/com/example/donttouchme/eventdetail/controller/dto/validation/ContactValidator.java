package com.example.donttouchme.eventdetail.controller.dto.validation;

import com.example.donttouchme.event.domain.value.SendType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ContactValidator implements ConstraintValidator<ValidContactAndSendType, ContactValidatable> {
    //연락처 형식 정규 표현식
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{3,4}-\\d{4}$");

    //이메일 형식 정규 표현식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Override
    public void initialize(ValidContactAndSendType constraintAnnotation) {
    }

    @Override
    public boolean isValid(ContactValidatable request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        String contact = request.contact();
        SendType sendType = request.sendType();

        if (contact == null || sendType == null) {
            return true;
        }

        // sendType에 따라 적절한 정규 표현식 적용
        if (sendType.equals(SendType.EMAIL)) {
            return EMAIL_PATTERN.matcher(contact).matches();
        } else if (sendType.equals(SendType.PHONE)) {
            return PHONE_NUMBER_PATTERN.matcher(contact).matches();
        }

        return false;
    }
}
