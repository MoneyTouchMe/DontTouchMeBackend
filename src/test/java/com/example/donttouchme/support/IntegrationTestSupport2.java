package com.example.donttouchme.support;

import com.example.donttouchme.event.controller.dto.CreateEventRequest;
import com.example.donttouchme.event.controller.dto.UpdateEventRequest;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Tag;
import com.example.donttouchme.event.domain.TagEventDetail;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.event.domain.value.SendType;
import com.example.donttouchme.eventdetail.controller.dto.CreateEventDetailRequest;
import com.example.donttouchme.eventdetail.controller.dto.UpdateEventDetailRequest;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public abstract class IntegrationTestSupport2 {
    protected Member createTestMember() {
        return Member.builderWithPassword()
                .name("testMember")
                .email("testMember@test.com")
                .role(ROLE.user)
                .loginProvider(LoginProvider.original)
                .contact("010-1234-5678")
                .password("test123131231")
                .builderWithPassword();
    }

    protected Location createTestLocation() {
        return Location.builder()
                .longitude(123.1241212)
                .latitude(23.2222222)
                .address("경기도 안양시")
                .build();
    }

    protected EventInfo createTestEventInfo() {
        return EventInfo.builder()
                .isHistory(true)
                .isTag(true)
                .isSide(true)
                .isSend(true)
                .isPrice(true)
                .isImage(true)
                .isName(true)
                .isType(true)
                .build();
    }

    protected Tag createTestTag(Event event) {
        return Tag.builder()
                .value("test")
                .event(event)
                .build();
    }

    protected Target createTestTarget(Event event) {
        return Target.builder()
                .value("test")
                .event(event)
                .build();
    }

    protected EventDetail createTestEventDetail(Event event, Target target) {
        return EventDetail.builder()
                .contact("010-1111-1111")
                .type("입금")
                .image("imageurl/1231412/24342")
                .history("회비")
                .price("10")
                .event(event)
                .name("김희범")
                .target(target)
                .build();
    }

    protected TagEventDetail createTestTagEventDetail(EventDetail eventDetail, Tag tag) {
        return TagEventDetail.builder()
                .eventDetail(eventDetail)
                .tag(tag)
                .build();
    }

    protected CreateEventRequest createTestCreateEventRequest(Long memberId) {
        return new CreateEventRequest(
                memberId,
                "thumbnailurl/12314/test",
                "testEvent",
                "결혼식",
                LocalDate.now(),
                "경기도 안양시",
                11.1111111,
                22.2222222,
                10,
                "만원",
                true,
                true,
                true,
                true,
                List.of("결혼식", "태그", "테스트"),
                true,
                List.of("신랑측", "신부측", "신랑아버지", "신부아버지"),
                true,
                SendType.EMAIL
        );
    }

    protected UpdateEventRequest createTestUpdateEventRequest() {
        return new UpdateEventRequest(
                "thumbnailurl/12314/test",
                "testEvent2",
                "결혼식",
                LocalDate.now(),
                "경기도 안양시",
                11.1111111,
                22.2222222,
                10,
                "만원",
                true,
                true,
                true,
                true,
                List.of("결혼식", "태그", "테스트"),
                true,
                List.of("신랑측", "신부측", "신랑아버지", "신부아버지"),
                true,
                SendType.EMAIL
        );
    }

    protected CreateEventDetailRequest createTestEventDetailRequest(Long eventId) {
        return new CreateEventDetailRequest(
                eventId,
                "출금",
                "회비",
                "10",
                "김희범",
                List.of("태그"),
                "testImageUrl/12313124/123",
                "신랑측",
                SendType.EMAIL,
                "test@test.com"
        );
    }

    protected UpdateEventDetailRequest createTestUpdateEventDetailRequest() {
        return new UpdateEventDetailRequest(
                "입금",
                "회비update",
                "100",
                "김희범",
                List.of("태그"),
                "testImageUrl/12313124/123test",
                "신부측",
                SendType.EMAIL,
                "test2@test.com"
        );
    }
}
