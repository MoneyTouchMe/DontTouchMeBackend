package com.example.donttouchme.event.Integration;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.controller.dto.UpdateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Transactional
class EventCommandServiceTest extends IntegrationTestSupport2 {

    @Autowired
    EventCommandService eventCommandService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이벤트 생성 성공")
    void createEventSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);
        CreateEventRequest request = createTestCreateEventRequest(savedMember.getId());

        //when
        Event createdEvent = eventCommandService.createEvent(request);

        //then
        log.info("savedMember.getEvents() : {}", savedMember.getEvents().get(0).getId());
        assertThat(createdEvent).isNotNull();
        assertThat(savedMember.getEvents().get(0).getId()).isEqualTo(createdEvent.getId());
    }

    @Test
    @DisplayName("이벤트 삭제 성공")
    @Rollback(value = false)
    void deleteEvent() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);
        CreateEventRequest request = createTestCreateEventRequest(savedMember.getId());
        Event createdEvent = eventCommandService.createEvent(request);

        //when
        eventCommandService.deleteEvent(createdEvent.getId());

        //then
        assertThat(eventRepository.existsById(createdEvent.getId())).isFalse();
    }

    @Test
    @DisplayName("이벤트 수정 성공")
    @Rollback(value = false)
    void updateEvent() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);
        CreateEventRequest request = createTestCreateEventRequest(savedMember.getId());

        Event createdEvent = eventCommandService.createEvent(request);
        assertThat(createdEvent.getEventName()).isEqualTo("testEvent");

        List<String> tagList = List.of("결혼식", "태그", "테스트");

        UpdateEventRequest updateRequest = createTestUpdateEventRequest();

        eventCommandService.updateEvent(
                createdEvent.getId(),
                updateRequest
        );

        assertThat(createdEvent.getEventName()).isEqualTo("testEvent2");
        boolean allExist = tagList.stream().allMatch(
                value -> createdEvent.getTags().stream().anyMatch(tag -> tag.getValue().equals(value))
        );
        assertThat(allExist).isTrue();
    }
}