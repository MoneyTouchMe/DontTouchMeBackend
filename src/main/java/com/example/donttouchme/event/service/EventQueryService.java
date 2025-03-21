package com.example.donttouchme.event.service;

import com.example.donttouchme.event.controller.dto.FindEventListResponse;
import com.example.donttouchme.event.controller.dto.FindEventResponse;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Tag;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventQueryService {
    private final EventRepository eventRepository;

    //no offset으로 페이징 구현
    public FindEventListResponse findEventList(
            final Long memberId, final Long lastEventId, final int pageSize
    ) {
        return eventRepository.paginationNoOffset(memberId, lastEventId, pageSize);
    }

    public FindEventResponse findEvent(final Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다.")
        );

        List<String> tags = new ArrayList<>();
        for (Tag tag : event.getTags()) {
            tags.add(tag.getValue());
        }

        List<String> targets = new ArrayList<>();
        for (Target target : event.getTargets()) {
            targets.add(target.getValue());
        }

        return FindEventResponse.from(event, tags, targets);
    }
}
