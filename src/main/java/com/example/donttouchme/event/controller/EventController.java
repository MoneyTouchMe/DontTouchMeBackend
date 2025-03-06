package com.example.donttouchme.event.controller;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.service.EventCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventCommandService eventService;

    @PostMapping("/") //이벤트 생성
    public ResponseEntity<Long> createEvent(
            @RequestBody @Validated final CreateEventRequest request
    ) {
        Event createdEvent = eventService.createEvent(request);
        return ResponseEntity.ok(createdEvent.getId());
    }
}
