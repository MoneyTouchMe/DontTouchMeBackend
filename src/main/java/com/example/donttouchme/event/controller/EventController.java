package com.example.donttouchme.event.controller;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.service.EventCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController implements EventControllerSwagger {
    private final EventCommandService eventService;

    @PostMapping("/")
    public ResponseEntity<Long> createEvent(
            @RequestBody @Validated final CreateEventRequest request
    ) {
        Event createdEvent = eventService.createEvent(request);
        return ResponseEntity.ok(createdEvent.getId());
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable final Long eventId
    ) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build(); //성공 시 204 No Content 응답
    }
}
