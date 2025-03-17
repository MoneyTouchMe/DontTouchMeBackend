package com.example.donttouchme.eventdetail;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.repository.EventDetailRepository;
import com.example.donttouchme.eventdetail.service.EventDetailCommandService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
public class EventDetailCommandServiceTest extends IntegrationTestSupport {
    @Autowired
    EventDetailCommandService eventDetailCommandService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventDetailRepository eventDetailRepository;

    @Test
    @DisplayName("EventDetail 생성 성공")
    void createEventDetailSuccess() {
        //given
        Member testMember = createTestMemberForEvent();
        Member savedMember = memberRepository.save(testMember);

        Event testEvent = createTestEvent(savedMember);
        Event savedEvent = eventRepository.save(testEvent);

        //when
        EventDetail createdEventDetail = eventDetailCommandService.createEventDetail(
                new CreateEventDetailRequest(
                        savedEvent.getId(),
                        "출금",
                        "회비",
                        "10",
                        "김희범",
                        List.of("결혼식", "테스트"),
                        "testImageUrl/12313124/123",
                        "신부측",
                        "010-1111-2222"
                )
        );

        //then
        Assertions.assertThat(createdEventDetail.getPrice()).isEqualTo("10");
    }

    @Test
    @Rollback(value = false)
    @DisplayName("EventDetail 삭제 성공")
    void deleteEventDetailSuccess() {
        //given
        Member testMember = createTestMemberForEvent();
        Member savedMember = memberRepository.save(testMember);

        Event testEvent = createTestEvent(savedMember);
        Target testTarget = createTestTarget();
        List<Target> targets = new ArrayList<>();
        targets.add(testTarget);
        testEvent.setTargets(targets);
        Event savedEvent = eventRepository.save(testEvent);

        createTestEventDetail(savedEvent, null);
        EventDetail savedEventDetail = eventDetailCommandService.createEventDetail(
                new CreateEventDetailRequest(
                        savedEvent.getId(),
                        "출금",
                        "회비",
                        "10",
                        "김희범",
                        List.of("결혼식", "테스트"),
                        "testImageUrl/12313124/123",
                        "신부측",
                        "010-1111-2222"
                )
        );

        //when
        Assertions.assertThat(savedEventDetail.getDeletedAt()).isNull();
        eventDetailCommandService.deleteEventDetail(savedEventDetail.getId());

        //then
        Assertions.assertThat(eventDetailRepository.findById(savedEventDetail.getId())).isNotPresent();
    }
}
