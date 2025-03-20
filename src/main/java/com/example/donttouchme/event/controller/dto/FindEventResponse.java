package com.example.donttouchme.event.controller.dto;

import com.example.donttouchme.event.domain.Event;

import java.time.LocalDate;
import java.util.List;

public record FindEventResponse(
        String thumbnailUrl,

        String eventName,

        String eventType,

        LocalDate eventDate,

        String address,

        Integer participants,

        List<String> eventInfoItems, //입출금 항목 (토글 중 ON으로 된 항목)

        String amountUnit,

        String sendType,

        List<String> tags,

        List<String> targets

) {
    public static FindEventResponse from(Event event, List<String> tags, List<String> targets) {
        return new FindEventResponse(
                event.getThumbnailUrl(),
                event.getEventName(),
                event.getEventType(),
                event.getEventDate(),
                event.getLocation().getAddress(),
                event.getParticipants(),
                event.getEventInfo().toCellValues(),
                event.getAmountUnit(),
                event.getSendType().toString(),
                tags,
                targets
        );
    }
}
