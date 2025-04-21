package com.example.donttouchme.send.controller.dto;

import java.util.List;

public record SendSMSRequest(
        List<RecipientListDto> recipients,
        String eventName
) {
}
