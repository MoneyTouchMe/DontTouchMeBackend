package com.example.donttouchme.event.Integration;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.controller.dto.FindEventListRequest;
import com.example.donttouchme.event.controller.dto.FindEventListResponse;
import com.example.donttouchme.event.controller.dto.FindEventResponse;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.event.service.EventQueryService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EventQueryServiceTest extends IntegrationTestSupport2 {
    @Autowired
    EventQueryService eventQueryService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventCommandService eventCommandService;

    @Test
    @DisplayName("No Offset 방식으로 이벤트 목록 조회")
    void findEventListSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = createTestCreateEventRequest(savedMember.getId());
        Event createdEvent1 = eventCommandService.createEvent(request);

        CreateEventRequest request2 = createTestCreateEventRequest(savedMember.getId());
        Event createdEvent2 = eventCommandService.createEvent(request2);

        CreateEventRequest request3 = createTestCreateEventRequest(savedMember.getId());
        Event createdEvent3 = eventCommandService.createEvent(request3);


        FindEventListRequest listRequest = new FindEventListRequest(
                savedMember.getId(),
                null,
                3
        );

        //when
        FindEventListResponse eventList = eventQueryService.findEventList(listRequest);

        //then
        assertThat(eventList.events().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이벤트 상세 조회")
    void findEventSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = createTestCreateEventRequest(savedMember.getId());
        Event createdEvent = eventCommandService.createEvent(request);

        //when
        FindEventResponse event = eventQueryService.findEvent(createdEvent.getId());

        //then
        assertThat(event.eventName()).isEqualTo("testEvent");
        assertThat(event.eventInfoItems()).containsExactly(
                "입출금 분류", "입출금 내역명", "금액", "이름", "태그", "입금대상", "감사장"
        );
    }
}