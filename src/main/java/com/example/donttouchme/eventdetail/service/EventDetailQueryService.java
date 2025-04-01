package com.example.donttouchme.eventdetail.service;

import com.example.donttouchme.event.domain.TagEventDetail;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailListResponse;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailResponse;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.repository.EventDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public FindEventDetailResponse findEventDetail(final Long eventDetailId) {
        EventDetail eventDetail = eventDetailRepository.findById(eventDetailId).orElseThrow(
                () -> new IllegalArgumentException("입출금 내역 정보를 찾을 수 없습니다.")
        );

        List<String> tags = new ArrayList<>();
        for (TagEventDetail tagEventDetail : eventDetail.getTagEventDetails()) {
            tags.add(tagEventDetail.getTag().getValue());
        }

        Target target = eventDetail.getTarget();

        return FindEventDetailResponse.from(eventDetail, tags, target.getValue());
    }
}
