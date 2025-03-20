package com.example.donttouchme.event.service;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;

import com.example.donttouchme.event.controller.dto.UpdateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Tag;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.repository.TagRepository;
import com.example.donttouchme.event.repository.TargetRepository;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class EventCommandService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final TargetRepository targetRepository;

    public Event createEvent(final CreateEventRequest request) {
        Member member = memberRepository.findById(request.memberId()).orElseThrow(
                () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. memberId: " + request.memberId())
        );

        boolean tagIsNull = Objects.isNull(request.tags());
        boolean targetIsNull = Objects.isNull(request.targets());

        Event event = request.toEntity(member);

        if (!tagIsNull) {
            for (String tag : request.tags()) {
                Tag.builder()
                        .value(tag)
                        .event(event)
                        .build();
            }
        }

        if (!targetIsNull) {
            for (String target : request.targets()) {
                Target.builder()
                        .value(target)
                        .event(event)
                        .build();
            }
        }

        return eventRepository.save(event);
    }

    //논리 삭제
    public void deleteEvent(final Long eventId) {
        Event findEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다. eventId: " + eventId)
        );
        eventRepository.delete(findEvent);
    }

    public void updateEvent(final Long eventId, final UpdateEventRequest request) {
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

        findEvent.update(request, location, eventInfo);

        //기존 저장된 Tag를 삭제 후 새로 입력받은 Tag를 생성
        tagRepository.deleteAll(findEvent.getTags());
        if (!tagIsNull) {
            for (String tag : request.tags()) {
                Tag.builder()
                        .value(tag)
                        .event(findEvent)
                        .build();
            }
        }

        //기존 저장된 Target을 삭제 후 새로 입력받은 Target을 생성
        targetRepository.deleteAll(findEvent.getTargets());
        if (!targetIsNull) {
            for (String target : request.targets()) {
                Target.builder()
                        .value(target)
                        .event(findEvent)
                        .build();
            }
        }
    }
}
