package com.example.donttouchme.eventdetail.service;

import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailListResponse;
import com.example.donttouchme.eventdetail.repository.EventDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventDetailQueryService {
    private final EventDetailRepository eventDetailRepository;

    //no offset 방식의 페이징 구현
    public FindEventDetailListResponse findEventDetailList(
            final Long eventId, final Long lastEventDetailId, final int pageSize
    ) {
        return eventDetailRepository.paginationNoOffset(eventId, lastEventDetailId, pageSize);
    }
}
