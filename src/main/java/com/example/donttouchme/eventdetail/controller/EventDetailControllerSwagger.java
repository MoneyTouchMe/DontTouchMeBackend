package com.example.donttouchme.eventdetail.controller;

import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailListResponse;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailResponse;
import com.example.donttouchme.eventdetail.controller.dto.UpdateEventDetailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "EventDetail 관련 API", description = "입출금 내역 관련 API")
public interface EventDetailControllerSwagger {
    @Operation(
            summary = "입출금 내역 추가 API",
            description = "입출금 내역을 생성합니다."
    )
    public ResponseEntity<Long> createEventDetail(
            @RequestBody @Validated final CreateEventDetailRequest request
    );

    @Operation(
            summary = "입출금 내역 삭제 API",
            description = "입출금 내역을 삭제합니다."
    )
    public ResponseEntity<Void> deleteEventDetail(
            @PathVariable final Long eventDetailId
    );

    @Operation(
            summary = "입출금 내역 수정 API",
            description = "입출금 내역을 수정합니다."
    )
    public ResponseEntity<Void> updateEventDetail(
            @PathVariable final Long eventDetailId,
            @Validated @RequestBody final UpdateEventDetailRequest request
    );

    @Operation(
            summary = "입출금 내역 목록 조회 API",
            description = "입출금 내역 목록을 조회합니다."
    )
    public FindEventDetailListResponse findEventDetailList(
            @RequestParam final Long eventId,
            @RequestParam(required = false) final Long lastEventDetailId,
            @RequestParam(defaultValue = "20") final int pageSize
    );

    @Operation(
            summary = "입출금 내역 상세 조회 API",
            description = "입출금 내역 상세 정보를 조회합니다."
    )
    public FindEventDetailResponse findEventDetail(
            @PathVariable final Long eventDetailId
    );
}
