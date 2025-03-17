package com.example.donttouchme.eventdetail.controller;

import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.service.EventDetailCommandService;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/event/detail")
@RequiredArgsConstructor
public class EventDetailController implements EventDetailControllerSwagger {
    private final EventDetailCommandService eventDetailCommandService;

    @PostMapping("/")
    public ResponseEntity<Long> createEventDetail(
            @RequestBody @Validated final CreateEventDetailRequest request
    ) {
        EventDetail createdEventDetail = eventDetailCommandService.createEventDetail(request);
        return ResponseEntity.ok(createdEventDetail.getId());
    }
}
