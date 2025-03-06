package com.example.donttouchme.event.Integration;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
class EventCommandServiceTest extends IntegrationTestSupport {

    @Autowired
    EventCommandService eventCommandService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Tag, Side가 없는 경우의 Event 생성 성공")
    void createEventWithoutTagsAndTargetsSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = new CreateEventRequest(
                savedMember.getId(),
                null,
                "testEvent",
                "결혼식",
                LocalDate.now(),
                "경기도 안양시",
                11.1111,
                22.2222,
                null,
                false,
                false,
                false,
                false,
                null,
                false,
                null,
                false,
                null
        );

        //when
        Event createdEvent = eventCommandService.createEvent(request);

        //then
        Assertions.assertThat(createdEvent).isNotNull();

    }

    @Test
    @DisplayName("Target만 없는 경우의 Event 생성 성공")
    void createEventWithoutTagsSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = new CreateEventRequest(
                savedMember.getId(),
                null,
                "testEvent",
                "결혼식",
                LocalDate.now(),
                "경기도 안양시",
                11.1111,
                22.2222,
                null,
                false,
                false,
                false,
                false,
                null,
                false,
                List.of("신부측, 신랑측, 신부아버지측"),
                false,
                null
        );

        //when
        Event createdEvent = eventCommandService.createEvent(request);
        List<Event> newEvents = savedMember.getEvents();
        newEvents.add(createdEvent);
        savedMember.setEvents(newEvents);

        //then
        Assertions.assertThat(createdEvent).isNotNull();

    }

    @Test
    @DisplayName("Tag만 없는 경우의 Event 생성 성공")
    void createEventWithoutTargetSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = new CreateEventRequest(
                savedMember.getId(),
                null,
                "testEvent",
                "결혼식",
                LocalDate.now(),
                "경기도 안양시",
                11.1111,
                22.2222,
                null,
                false,
                false,
                false,
                false,
                Arrays.asList("결혼식, 서울"),
                false,
                null,
                false,
                null
        );

        //when
        Event createdEvent = eventCommandService.createEvent(request);
        List<Event> newEvents = savedMember.getEvents();
        newEvents.add(createdEvent);
        savedMember.setEvents(newEvents);

        //then
        Assertions.assertThat(createdEvent).isNotNull();

    }
    @Test
    @DisplayName("Tag, Target 모두 있는 경우 Event 생성 성공")
    void createEventSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = new CreateEventRequest(
                savedMember.getId(),
                null,
                "testEvent",
                "결혼식",
                LocalDate.now(),
                "경기도 안양시",
                11.1111,
                22.2222,
                null,
                false,
                false,
                false,
                false,
                Arrays.asList("결혼식, 서울"),
                false,
                Arrays.asList("신부측, 신랑측, 신부아버지측"),
                false,
                null
        );

        //when
        Event createdEvent = eventCommandService.createEvent(request);
        List<Event> newEvents = savedMember.getEvents();
        newEvents.add(createdEvent);
        savedMember.setEvents(newEvents);

        //then
        Assertions.assertThat(createdEvent).isNotNull();

    }
}