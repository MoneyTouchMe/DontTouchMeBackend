package com.example.donttouchme.event.controller.dto;

import java.time.LocalDate;

public record EventListDto(
        Long eventId,

        String eventName,

        LocalDate eventDate,

        String thumbnailUrl,

        String eventType,

        String address
) {
}
