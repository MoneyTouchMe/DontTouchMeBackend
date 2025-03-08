package com.example.donttouchme.event.controller.dto;

import java.util.List;

public record FindEventListResponse(
        List<EventListDto> events, //이벤트 목록
        Long lastEventId //다음 요청을 위한 마지막 ID
) {
}
