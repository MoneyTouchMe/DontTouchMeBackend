package com.example.donttouchme.eventdetail;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.repository.TargetRepository;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.controller.dto.UpdateEventDetailRequest;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.repository.EventDetailRepository;
import com.example.donttouchme.eventdetail.service.EventDetailCommandService;
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

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    TargetRepository targetRepository;

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
        assertThat(createdEventDetail.getPrice()).isEqualTo("10");
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
        assertThat(eventDetailRepository.findById(createdEventDetail.getId())).isPresent();
        eventDetailCommandService.deleteEventDetail(createdEventDetail.getId());

        //then
        assertThat(eventDetailRepository.findById(createdEventDetail.getId())).isNotPresent();
    }

    @Test
    @DisplayName("입출력 내역 수정 성공")
    @Rollback(value = false)
    void updateEventDetail() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);
        Event createdEvent = eventCommandService.createEvent(createTestCreateEventRequest(savedMember.getId()));
        EventDetail createdEventDetail = eventDetailCommandService.createEventDetail(createTestEventDetailRequest(createdEvent.getId()));

        UpdateEventDetailRequest request = createTestUpdateEventDetailRequest();
        Target target = null;
        if (request.target() != null) {
            target = targetRepository.findByValueAndEventId(request.target(), createdEvent.getId()).get();
        }

        //when
        createdEventDetail.update(request, target);

        //then
        assertThat(createdEventDetail.getType()).isEqualTo("입금");
    }

    @Test
    @DisplayName("중복 Target 값 설정 성공")
    public void duplicatedTargetSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        Event createdEvent = eventCommandService.createEvent(createTestCreateEventRequest(savedMember.getId()));


        //when
        CreateEventDetailRequest request = createTestEventDetailRequest(createdEvent.getId());
        EventDetail createdEventDetail = eventDetailCommandService.createEventDetail(request);
        EventDetail createdEventDetail2 = eventDetailCommandService.createEventDetail(request);

        //then
        assertThat(createdEventDetail.getTarget().getValue()).isEqualTo(createdEventDetail2.getTarget().getValue());
    }
}
