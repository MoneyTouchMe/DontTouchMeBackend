package com.example.donttouchme.event.service;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;

import com.example.donttouchme.event.controller.dto.UpdateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Tag;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class EventCommandService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    public Event createEvent(final CreateEventRequest request) {
        Member member = memberRepository.findById(request.memberId()).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. memberId: " + request.memberId())
        );

        Location location = new Location(request.latitude(), request.longitude(), request.address());
        boolean tagIsNull = Objects.isNull(request.tags());
        boolean targetIsNull = Objects.isNull(request.targets());

        EventInfo eventInfo = new EventInfo(
                request.isType(),
                request.isHistory(),
                request.isPrice(),
                request.isName(),
                !tagIsNull, //tag 값이 없으면 토글 OFF로 처리
                request.isImage(),
                !targetIsNull, //side 값이 없으면 토글 OFF로 처리
                request.isSend()
        );

        Event event = Event.builderWithoutTagAndTarget()
                .thumbnailUrl(request.thumbnailUrl())
                .eventName(request.eventName())
                .eventType(request.eventType())
                .eventDate(request.eventDate())
                .location(location)
                .eventInfo(eventInfo)
                .participants(request.participants())
                .member(member)
                .builderWithoutTagAndTarget();

        if (!tagIsNull) {
            List<Tag> tags = new ArrayList<>(event.getTags());
            for (String tag : request.tags()) {
                Tag createdTag = Tag.builder()
                        .value(tag)
                        .event(event)
                        .build();
                tags.add(createdTag);
            }
            event.setTags(tags);
        }

        if (!targetIsNull) {
            List<Target> targets = new ArrayList<>(event.getTargets());
            for (String target : request.targets()) {
                Target createdTarget = Target.builder()
                        .value(target)
                        .event(event)
                        .build();
                targets.add(createdTarget);
            }
            event.setTargets(targets);
        }

        List<Event> events = new ArrayList<>(member.getEvents());
        events.add(event);
        member.setEvents(events);

        return eventRepository.save(event);
    }

    //논리 삭제
    public void deleteEvent(Long eventId) {
        Event findEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다. eventId: " + eventId)
        );
        eventRepository.delete(findEvent);
    }

    public void updateEvent(Long eventId, UpdateEventRequest request) {
        Event findEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다. eventId: " + eventId)
        );

        Location location = new Location(request.latitude(), request.longitude(), request.address());
        boolean tagIsNull = Objects.isNull(request.tags());
        boolean targetIsNull = Objects.isNull(request.targets());

        EventInfo eventInfo = new EventInfo(
                request.isType(),
                request.isHistory(),
                request.isPrice(),
                request.isName(),
                !tagIsNull, //tag 값이 없으면 토글 OFF로 처리
                request.isImage(),
                !targetIsNull, //side 값이 없으면 토글 OFF로 처리
                request.isSend()
        );

        findEvent.updateEvent(
                request.thumbnailUrl(),
                request.eventName(),
                request.eventType(),
                request.eventDate(),
                location,
                eventInfo,
                request.participants()
        );


        if (!tagIsNull) {
            List<Tag> tags = new ArrayList<>(findEvent.getTags());
            for (String tag : request.tags()) {
                Tag createdTag = Tag.builder()
                        .value(tag)
                        .event(findEvent)
                        .build();
                tags.add(createdTag);
            }
            findEvent.setTags(tags);
        }

        if (!targetIsNull) {
            List<Target> targets = new ArrayList<>(findEvent.getTargets());
            for (String target : request.targets()) {
                Target createdTarget = Target.builder()
                        .value(target)
                        .event(findEvent)
                        .build();
                targets.add(createdTarget);
            }
            findEvent.setTargets(targets);
        }
    }
}
