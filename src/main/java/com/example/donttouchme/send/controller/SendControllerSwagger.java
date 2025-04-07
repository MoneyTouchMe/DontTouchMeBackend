package com.example.donttouchme.send.controller;

import com.example.donttouchme.send.controller.dto.FindRecipientListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
