package com.example.donttouchme.event.controller.dto;

import java.time.LocalDate;

public record FindEventListResponse(
        long eventId,
        String eventName,
        LocalDate eventDate,
        String thumbnailUrl,
        String eventType,
        String address
) {
}
