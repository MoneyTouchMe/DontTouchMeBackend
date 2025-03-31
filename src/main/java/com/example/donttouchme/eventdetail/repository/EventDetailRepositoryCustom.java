package com.example.donttouchme.eventdetail.repository;

import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailListResponse;

public interface EventDetailRepositoryCustom {
    FindEventDetailListResponse paginationNoOffset(
            final Long eventId,
            final Long lastEventDetailId,
            final int pageSize
    );
}
