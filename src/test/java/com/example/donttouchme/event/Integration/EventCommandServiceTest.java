package com.example.donttouchme.event.Integration;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class EventCommandServiceTest extends IntegrationTestSupport {

    @Autowired
    EventCommandService eventCommandService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
        eventRepository.deleteAll();
    }

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
        log.info("savedMember.getEvents() : {}", savedMember.getEvents().get(0).getId());
        assertThat(createdEvent).isNotNull();
        assertThat(savedMember.getEvents().get(0).getId()).isEqualTo(createdEvent.getId());
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

        //then
        log.info("savedMember.getEvents() : {}", savedMember.getEvents().get(0).getId());
        assertThat(createdEvent).isNotNull();
        assertThat(savedMember.getEvents().get(0).getId()).isEqualTo(createdEvent.getId());

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

        //then
        log.info("savedMember.getEvents() : {}", savedMember.getEvents().get(0).getId());
        assertThat(createdEvent).isNotNull();
        assertThat(savedMember.getEvents().get(0).getId()).isEqualTo(createdEvent.getId());
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
                "testEvent2",
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

        //then
        log.info("savedMember.getEvents() : {}", savedMember.getEvents().get(0).getId());
        assertThat(createdEvent).isNotNull();
        assertThat(savedMember.getEvents().get(0).getId()).isEqualTo(createdEvent.getId());
    }

    @Test
    @DisplayName("이벤트 삭제 성공")
    @Commit
    void deleteEvent() {
        //given
        Member savedMember = memberRepository.save(createTestMember());
        Event savedEvent = eventRepository.save(createTestEvent(savedMember));
        assertThat(eventRepository.existsById(savedEvent.getId())).isTrue();

        //when
        eventCommandService.deleteEvent(savedEvent.getId());

        //then
        assertThat(eventRepository.existsById(savedEvent.getId())).isFalse();
    }

}