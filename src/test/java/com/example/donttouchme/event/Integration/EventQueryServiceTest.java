package com.example.donttouchme.event.Integration;

import com.example.donttouchme.event.controller.dto.FindEventListRequest;
import com.example.donttouchme.event.controller.dto.FindEventListResponse;
import com.example.donttouchme.event.controller.dto.FindEventResponse;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
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

    @Test
    @DisplayName("No Offset 방식으로 이벤트 목록 조회")
    void findEventListSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        Event testEvent = createTestEvent(savedMember);
        eventRepository.save(testEvent);

        Event testEvent2 = createTestEvent(savedMember);
        eventRepository.save(testEvent2);

        Event testEvent3 = createTestEvent(savedMember);
        eventRepository.save(testEvent3);

        FindEventListRequest request = new FindEventListRequest(
                savedMember.getId(),
                null,
                3
        );

        //when
        FindEventListResponse eventList = eventQueryService.findEventList(request);

        //then
        assertThat(eventList.events().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이벤트 상세 조회")
    void findEventSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        Event testEvent = createTestEvent(savedMember);
        Event savedEvent = eventRepository.save(testEvent);

        //when
        FindEventResponse event = eventQueryService.findEvent(savedEvent.getId());

        //then
        assertThat(event.eventName()).isEqualTo("test");
        assertThat(event.eventInfoItems()).containsExactly(
                "입출금 분류", "입출금 내역명", "금액", "이름", "태그", "입금대상", "감사장"
        );
    }
}