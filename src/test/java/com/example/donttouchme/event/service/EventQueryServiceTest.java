package com.example.donttouchme.event.service;

import com.example.donttouchme.event.controller.dto.FindEventListRequest;
import com.example.donttouchme.event.controller.dto.FindEventListResponse;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class EventQueryServiceTest extends IntegrationTestSupport {
    @Autowired
    EventQueryService eventQueryService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @BeforeEach
    void clear() {
        memberRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @DisplayName("No Offset 방식으로 이벤트 목록 조회")
    void findEventListSuccess() {
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
        List<FindEventListResponse> eventList = eventQueryService.findEventList(request);

        //then
        Assertions.assertThat(eventList.size()).isEqualTo(3);
    }
}