package com.example.donttouchme.send;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.service.EventCommandService;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.eventdetail.service.EventDetailCommandService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.send.controller.dto.FindRecipientListResponse;
import com.example.donttouchme.send.service.SendQueryService;
import com.example.donttouchme.support.IntegrationTestSupport2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SendQueryServiceTest extends IntegrationTestSupport2 {
    @Autowired
    SendQueryService sendQueryService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventCommandService eventCommandService;

    @Autowired
    EventDetailCommandService eventDetailCommandService;

    @Test
    @DisplayName("Offset 방식으로 이벤트별 감사장 발송 대상 목록 조회")
    public void findRecipientListSuccess() {
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
        FindRecipientListResponse response = sendQueryService.findRecipientList(createdEvent.getId());

        //then
        assertThat(response.recipients().size()).isEqualTo(3);
    }
}
