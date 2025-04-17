package com.example.donttouchme.send.controller;

import com.example.donttouchme.send.controller.dto.FindRecipientListResponse;
import com.example.donttouchme.send.controller.dto.SendEmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "감사장 관련 API", description = "감사장 관련 API")
public interface SendControllerSwagger {
    @Operation(
            summary = "감사장 발송 대상 목록 조회 API",
            description = "감사장 발송 대상 목록을 조회합니다."
    )
    public FindRecipientListResponse findRecipientList(
            @RequestParam final Long eventId
    );

    @Operation(
            summary = "감사장 이메일 전송 API",
            description = "감사장을 이메일로 전송합니다."
    )
    public ResponseEntity<String> sendAppreciationEmail(
            @RequestBody final SendEmailRequest request
    );

    /*@Operation(
            summary = "감사장 문자 메시지 전송 API",
            description = "감사장을 문자 메시지로 전송합니다."
    )
    public ResponseEntity<String> sendAppreciationSMS(
            @RequestBody final SendSMSRequest request
    );*/
}
