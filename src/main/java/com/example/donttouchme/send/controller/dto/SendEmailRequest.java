package com.example.donttouchme.send.controller.dto;

import java.util.List;

public record SendEmailRequest(
        List<RecipientListDto> recipients,
        String eventName,
        String fromEmail
) {
}
