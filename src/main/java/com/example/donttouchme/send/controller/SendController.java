package com.example.donttouchme.send.controller;

import com.example.donttouchme.send.controller.dto.*;
import com.example.donttouchme.send.service.SendQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/send")
@RequiredArgsConstructor
public class SendController implements SendControllerSwagger {
    private final SendQueryService sendQueryService;

    @GetMapping("/recipient/list")
    public FindRecipientListResponse findRecipientList(@RequestParam final Long eventId) {
        return sendQueryService.findRecipientList(eventId);
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendAppreciationEmail(
            @RequestBody final SendEmailRequest request
    ) {
        EmailSendResult result = sendQueryService.sendAppreciationEmail(request);
        if (result.failCnt() > 0) {
            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .body(result.message());
        }
        return ResponseEntity.ok(result.message());
    }

    @PostMapping("/sms")
    public ResponseEntity<String> sendAppreciationSMS(
            @RequestBody final SendSMSRequest request
    ) {
        SMSSendResult result = sendQueryService.sendAppreciationSMS(request);

        if (result.failCnt() > 0) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(result.message());  // 206
        }
        return ResponseEntity.ok(result.message());
    }
}
