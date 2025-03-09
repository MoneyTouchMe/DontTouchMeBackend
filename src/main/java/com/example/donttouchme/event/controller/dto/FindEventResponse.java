package com.example.donttouchme.event.controller.dto;

import java.time.LocalDate;
import java.util.List;

public record FindEventResponse(
        String thumbnailUrl,

        String eventName,

        String eventType,

        LocalDate eventDate,

        String address,

        Integer participants,

        List<String> eventInfoItems //입출금 항목 (토글 중 ON으로 된 항목)
) {
}
