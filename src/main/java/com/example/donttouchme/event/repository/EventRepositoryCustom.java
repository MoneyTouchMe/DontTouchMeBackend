package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.controller.dto.FindEventListResponse;

import java.util.List;

public interface EventRepositoryCustom {
    //No Offset으로 페이징 구현
    List<FindEventListResponse> paginationNoOffset(
            final Long memberId,
            final Long lastEventId,
            final int pageSize);
}
