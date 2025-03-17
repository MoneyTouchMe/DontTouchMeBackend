package com.example.donttouchme.eventdetail.controller;

import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "EventDetail 관련 API", description = "입출금 내역 관련 API")
public interface EventDetailControllerSwagger {
    @Operation(
            summary = "입출금 내역 추가 API",
            description = "입출금 내역을 생성합니다."
    )
    public ResponseEntity<Long> createEventDetail(
            @RequestBody @Validated final CreateEventDetailRequest request
    );
}
