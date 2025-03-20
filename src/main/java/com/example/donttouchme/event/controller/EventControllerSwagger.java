package com.example.donttouchme.event.controller;

import com.example.donttouchme.event.controller.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Event 관련 API", description = "Event 관련 API")
public interface EventControllerSwagger {

    @Operation(
            summary = "이벤트 생성 API",
            description = "새로운 이벤트를 생성합니다."
    )
    public ResponseEntity<Long> createEvent(
            @RequestBody @Validated final CreateEventRequest request
    );

    @Operation(
            summary = "이벤트 삭제 API",
            description = "이벤트를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            description = "성공적으로 삭제",
                            responseCode = "204"
                    )
            }
    )
    public ResponseEntity<Void> deleteEvent(
            @PathVariable final Long eventId
    );

    @Operation(
            summary = "이벤트 수정 API",
            description = "이벤트 정보를 수정합니다."
    )
    public ResponseEntity<Void> updateEvent(
            @PathVariable final Long eventId,
            @Validated @RequestBody final UpdateEventRequest request
    );

    @Operation(
            summary = "이벤트 목록 조회 API",
            description = "No Offset 방식으로 이벤트 목록을 조회합니다."
    )
    public FindEventListResponse findEventList(
            @RequestParam Long memberId,
            @RequestParam(required = false) Long lastEventId,
            @RequestParam(defaultValue = "20") int pageSIze
    );

    @Operation(
            summary = "이벤트 상세 조회 API",
            description = "이벤트 상세 정보를 조회합니다."
    )
    public FindEventResponse findEvent(
            @PathVariable final Long eventId
    );
}