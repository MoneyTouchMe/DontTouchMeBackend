package com.example.donttouchme.send.controller;

import com.example.donttouchme.send.controller.dto.FindRecipientListResponse;
import com.example.donttouchme.send.service.SendQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/send")
@RequiredArgsConstructor
public class SendController implements SendControllerSwagger {
    private final SendQueryService sendQueryService;

    @GetMapping("/recipient/list")
    public FindRecipientListResponse findRecipientList(@RequestParam final Long eventId) {
        return sendQueryService.findRecipientList(eventId);
    }
}
