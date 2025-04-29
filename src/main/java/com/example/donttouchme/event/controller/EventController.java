package com.example.donttouchme.event.controller;

import com.example.donttouchme.common.config.security.AuthMember;
import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.controller.dto.FindEventListResponse;
import com.example.donttouchme.event.controller.dto.FindEventResponse;
import com.example.donttouchme.event.controller.dto.UpdateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.event.service.EventQueryService;
import com.example.donttouchme.member.domain.Member;
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
            @AuthMember final Member member,
            @RequestParam(required = false) final Long lastEventId,
            @RequestParam(defaultValue = "20") final int pageSize
    ) {
        return eventQueryService.findEventList(member.getId(), lastEventId, pageSize);
    }

    @GetMapping("/{eventId}")
    public FindEventResponse findEvent(
            @PathVariable final Long eventId
    ) {
        return eventQueryService.findEvent(eventId);
    }
}
