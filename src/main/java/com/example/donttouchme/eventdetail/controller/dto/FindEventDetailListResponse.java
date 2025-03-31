package com.example.donttouchme.eventdetail.controller.dto;

import java.util.List;

public record FindEventDetailListResponse(
        List<EventDetailListDto> eventDetails, //입출금 목록
        Long lastEventDetailId
) {
}
