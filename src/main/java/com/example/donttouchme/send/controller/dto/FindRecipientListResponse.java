package com.example.donttouchme.send.controller.dto;

import java.util.List;

public record FindRecipientListResponse(
        List<RecipientListDto> recipients
) {
}
