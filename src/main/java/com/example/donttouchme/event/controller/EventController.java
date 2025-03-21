package com.example.donttouchme.event.controller;

import com.example.donttouchme.event.controller.dto.*;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.event.service.EventQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController implements EventControllerSwagger {
    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;

    @PostMapping
    public ResponseEntity<Long> createEvent(
            @RequestBody @Validated final CreateEventRequest request
    ) {
        Event createdEvent = eventCommandService.createEvent(request);
        return ResponseEntity.ok(createdEvent.getId());
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable final Long eventId
    ) {
        eventCommandService.deleteEvent(eventId);
        return ResponseEntity.noContent().build(); //성공 시 204 No Content 응답
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Void> updateEvent(
            @PathVariable final Long eventId,
            @Validated @RequestBody final UpdateEventRequest request
    ) {
        eventCommandService.updateEvent(eventId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public FindEventListResponse findEventList(
            @RequestParam final Long memberId,
            @RequestParam(required = false) final Long lastEventId,
            @RequestParam(defaultValue = "20") final int pageSIze
    ) {
        return eventQueryService.findEventList(memberId, lastEventId, pageSIze);
    }

    @GetMapping("/{eventId}")
    public FindEventResponse findEvent(
            @PathVariable final Long eventId
    ) {
        return eventQueryService.findEvent(eventId);
    }
}
