package com.example.donttouchme.eventdetail.service;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Tag;
import com.example.donttouchme.event.domain.TagEventDetail;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.repository.TagEventDetailRepository;
import com.example.donttouchme.event.repository.TagRepository;
import com.example.donttouchme.event.repository.TargetRepository;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.controller.dto.UpdateEventDetailRequest;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.repository.EventDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventDetailCommandService {
    private final EventDetailRepository eventDetailRepository;
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final TargetRepository targetRepository;
    private final TagEventDetailRepository tagEventDetailRepository;

    public EventDetail createEventDetail(final CreateEventDetailRequest request) {
        Event findEvent = eventRepository.findById(request.eventId()).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다. eventId : " + request.eventId())
        );

        //Target 엔티티 찾기
        Target findTarget = null;
        if (request.target() != null) {
            findTarget = targetRepository.findByValueAndEventId(request.target(), request.eventId()).get();
        }

        EventDetail eventDetail = request.toEntity(findEvent, findTarget);


        //Tag 엔티티 찾아서 TagEventDetails 연관관계 설정
        if (request.tags() != null) {
            for (String tag : request.tags()) {
                Tag findTag = tagRepository.findByValueAndEventId(tag, request.eventId()).get();
                tagEventDetailRepository.save(new TagEventDetail(eventDetail, findTag));
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

    public void updateEventDetail(
            final Long eventDetailId,
            final UpdateEventDetailRequest request
    ) {
        EventDetail findEventDetail = eventDetailRepository.findById(eventDetailId).orElseThrow(
                () -> new IllegalArgumentException("입출금 내역 정보를 찾을 수 없습니다. eventDetailId : " + eventDetailId)
        );

        //Target 엔티티 찾기
        Target findTarget = null;
        if (request.target() != null) {
            findTarget = targetRepository.findByValueAndEventId(request.target(), findEventDetail.getEvent().getId()).get();
        }

        findEventDetail.update(request, findTarget);

        //Tag 엔티티 찾아서 TagEventDetails 연관관계 설정
        if (request.tags() != null) {
            for (String tag : request.tags()) {
                Tag findTag = tagRepository.findByValueAndEventId(tag, findEventDetail.getEvent().getId()).get();
                tagEventDetailRepository.save(new TagEventDetail(findEventDetail, findTag));
            }
        }
    }

}
