package com.example.donttouchme.event.controller;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.controller.dto.CreateEventResponse;
import com.example.donttouchme.event.service.EventService;
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
    private final EventService eventService;

    @PostMapping("/")
    public ResponseEntity<CreateEventResponse> createEvent(
            @RequestBody @Validated final CreateEventRequest request
    ) {
        return null;
    }

}
