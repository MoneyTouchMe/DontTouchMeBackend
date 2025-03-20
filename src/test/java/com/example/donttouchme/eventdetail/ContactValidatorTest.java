package com.example.donttouchme.eventdetail;

import com.example.donttouchme.eventdetail.controller.dto.ContactValidator;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ContactValidatorTest {

    private ContactValidator contactValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        contactValidator = new ContactValidator();
        context = mock(ConstraintValidatorContext.class); // Mock 객체 생성
    }

    @Test
    void testValidEmail() {
        CreateEventDetailRequest request = new CreateEventDetailRequest(
                1L, "type", "history", "price", "name", List.of(), "imageUrl", "target", "이메일", "example@example.com"
        );
        assertTrue(contactValidator.isValid(request, context));
    }

    @Test
    void testInvalidEmail() {
        CreateEventDetailRequest request = new CreateEventDetailRequest(
                1L, "type", "history", "price", "name", List.of(), "imageUrl", "target", "이메일", "invalid-email"
        );
        assertFalse(contactValidator.isValid(request, context));
    }

    @Test
    void testValidPhone() {
        CreateEventDetailRequest request = new CreateEventDetailRequest(
                1L, "type", "history", "price", "name", List.of(), "imageUrl", "target", "문자", "010-1234-5678"
        );
        assertTrue(contactValidator.isValid(request, context));
    }

    @Test
    void testInvalidPhone() {
        CreateEventDetailRequest request = new CreateEventDetailRequest(
                1L, "type", "history", "price", "name", List.of(), "imageUrl", "target", "문자", "12345678"
        );
        assertFalse(contactValidator.isValid(request, context));
    }

    @Test
    void testNullContact() {
        CreateEventDetailRequest request = new CreateEventDetailRequest(
                1L, "type", "history", "price", "name", List.of(), "imageUrl", "target", "이메일", null
        );
        assertTrue(contactValidator.isValid(request, context));
    }

    @Test
    void testNullSendType() {
        CreateEventDetailRequest request = new CreateEventDetailRequest(
                1L, "type", "history", "price", "name", List.of(), "imageUrl", "target", null, "example@example.com"
        );
        assertTrue(contactValidator.isValid(request, context));
    }

    @Test
    void testInvalidSendType() {
        CreateEventDetailRequest request = new CreateEventDetailRequest(
                1L, "type", "history", "price", "name", List.of(), "imageUrl", "target", "invalidType", "example@example.com"
        );
        assertFalse(contactValidator.isValid(request, context));
    }
}

