package com.example.donttouchme.eventdetail;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.repository.EventDetailRepository;
import com.example.donttouchme.eventdetail.service.EventDetailCommandService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
public class EventDetailCommandServiceTest extends IntegrationTestSupport2 {
    @Autowired
    EventDetailCommandService eventDetailCommandService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventDetailRepository eventDetailRepository;

    @Autowired
    EventCommandService eventCommandService;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("EventDetail 생성 성공")
    void createEventDetailSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        Event createdEvent = eventCommandService.createEvent(createTestCreateEventRequest(savedMember.getId()));


        //when
        CreateEventDetailRequest request = createTestEventDetailRequest(createdEvent.getId());
        EventDetail createdEventDetail = eventDetailCommandService.createEventDetail(request);


        //then
        Assertions.assertThat(createdEventDetail.getPrice()).isEqualTo("10");
    }

    @Test
    @Rollback(value = false)
    @DisplayName("EventDetail 삭제 성공")
    void deleteEventDetailSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest eventRequest = createTestCreateEventRequest(savedMember.getId());
        Event createdEvent = eventCommandService.createEvent(eventRequest);

        CreateEventDetailRequest eventDetailRequest = createTestEventDetailRequest(createdEvent.getId());
        EventDetail createdEventDetail = eventDetailCommandService.createEventDetail(eventDetailRequest);


        //when
        Assertions.assertThat(eventDetailRepository.findById(createdEventDetail.getId())).isPresent();
        eventDetailCommandService.deleteEventDetail(createdEventDetail.getId());

        //then
        Assertions.assertThat(eventDetailRepository.findById(createdEventDetail.getId())).isNotPresent();
    }
}
