package com.example.donttouchme.eventdetail;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailListResponse;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailResponse;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.service.EventDetailCommandService;
import com.example.donttouchme.eventdetail.service.EventDetailQueryService;
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
public class EventDetailQueryServiceTest extends IntegrationTestSupport2 {
    @Autowired
    EventDetailQueryService eventDetailQueryService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventCommandService eventCommandService;

    @Autowired
    EventDetailCommandService eventDetailCommandService;

    @Test
    @DisplayName("No Offset 방식으로 입출금 내역 목록 조회")
    public void findEventDetailListSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = createTestCreateEventRequest(savedMember.getId());
        Event createdEvent = eventCommandService.createEvent(request);

        CreateEventDetailRequest request2 = createTestEventDetailRequest(createdEvent.getId());
        EventDetail createEventDetail1 = eventDetailCommandService.createEventDetail(request2);
        EventDetail createEventDetail2 = eventDetailCommandService.createEventDetail(request2);
        EventDetail createEventDetail3 = eventDetailCommandService.createEventDetail(request2);

        //when
        FindEventDetailListResponse eventDetailList = eventDetailQueryService.findEventDetailList(createdEvent.getId(), null, 20);
        FindEventDetailListResponse eventDetailList2 = eventDetailQueryService.findEventDetailList(createdEvent.getId(), null, 1);

        //then
        assertThat(eventDetailList.eventDetails().size()).isEqualTo(3);
        assertThat(eventDetailList2.eventDetails().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("입출금 내역 상세 조회")
    public void findEventDetailSuccess() {
        //given
        Member testMember = createTestMember();
        Member savedMember = memberRepository.save(testMember);

        CreateEventRequest request = createTestCreateEventRequest(savedMember.getId());
        Event savedEvent = eventCommandService.createEvent(request);

        CreateEventDetailRequest request2 = createTestEventDetailRequest(savedEvent.getId());
        EventDetail savedEventDetail = eventDetailCommandService.createEventDetail(request2);

        //when
        FindEventDetailResponse eventDetail = eventDetailQueryService.findEventDetail(savedEventDetail.getId());

        //then
        assertThat(eventDetail.contact()).isEqualTo("test@test.com");
    }
}
