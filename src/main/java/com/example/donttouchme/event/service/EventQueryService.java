package com.example.donttouchme.event.service;

import com.example.donttouchme.event.controller.dto.FindEventListRequest;
import com.example.donttouchme.event.controller.dto.FindEventListResponse;
import com.example.donttouchme.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventQueryService {
    private final EventRepository eventRepository;

    //no offset으로 페이징 구현
    public List<FindEventListResponse> findEventList(FindEventListRequest request) {
        return eventRepository.paginationNoOffset(request.memberId(), request.lastEventId(), request.pageSize());
    }
}
