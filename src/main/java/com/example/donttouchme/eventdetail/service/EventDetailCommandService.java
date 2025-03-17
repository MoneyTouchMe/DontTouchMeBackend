package com.example.donttouchme.eventdetail.service;

import com.example.donttouchme.event.domain.*;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.repository.TagRepository;
import com.example.donttouchme.event.repository.TargetRepository;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.repository.EventDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventDetailCommandService {
    private final EventDetailRepository eventDetailRepository;
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final TargetRepository targetRepository;

    public EventDetail createEventDetail(final CreateEventDetailRequest request) {
        Event findEvent = eventRepository.findById(request.eventId()).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다. eventId : " + request.eventId())
        );

        EventDetail eventDetail = EventDetail.builderOnlyField()
                .name(request.name())
                .history(request.history())
                .image(request.imageUrl())
                .price(request.price())
                .type(request.type())
                .contact(request.contact())
                .builderOnlyField();

        //Event 연관관계 연결
        List<EventDetail> eventDetails = new ArrayList<>(List.of(eventDetail));
        findEvent.setEventDetails(eventDetails);

        //Target 연관관계 연결
        if (request.target() != null) {
            Target findTarget = targetRepository.findByValue(request.target()).get();
            findTarget.setEventDetail(eventDetail);
        }

        //TagEventDetails 연관관계 연결
        if (request.tags() != null) {
            for (String tag : request.tags()) {
                Tag findTag = tagRepository.findByValue(tag).get();
                TagEventDetail tagEventDetail = new TagEventDetail(eventDetail, findTag);
                findTag.setTagEventDetail(tagEventDetail);
                eventDetail.setTagEventDetail(tagEventDetail);
            }
        }
        return eventDetailRepository.save(eventDetail);
    }

    public void deleteEventDetail(final Long eventDetailId) {
        EventDetail findEventDetail = eventDetailRepository.findById(eventDetailId).orElseThrow(
                () -> new IllegalArgumentException("입출금 내역 정보를 찾을 수 없습니다. eventDetailId : " + eventDetailId)
        );

        eventDetailRepository.delete(findEventDetail);
    }
}
