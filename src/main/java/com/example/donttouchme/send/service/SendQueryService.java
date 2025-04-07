package com.example.donttouchme.send.service;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.send.controller.dto.FindRecipientListResponse;
import com.example.donttouchme.send.controller.dto.RecipientListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SendQueryService {
    private final EventRepository eventRepository;

    public FindRecipientListResponse findRecipientList(final Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다.")
        );

        List<RecipientListDto> dto = new ArrayList<>();
        for (EventDetail eventDetail : event.getEventDetails()) {
            String name = eventDetail.getName();
            String contact = eventDetail.getContact();
            if (name != null && contact != null) {
                dto.add(new RecipientListDto(eventDetail.getName(), eventDetail.getContact()));
            }
        }
        return new FindRecipientListResponse(dto);
    }
}
