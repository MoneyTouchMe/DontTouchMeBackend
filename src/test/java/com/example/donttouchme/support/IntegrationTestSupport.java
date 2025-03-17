package com.example.donttouchme.support;


import com.example.donttouchme.event.domain.*;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import com.example.donttouchme.event.domain.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public abstract class IntegrationTestSupport {

    protected Member createTestMember() {
        return Member.builderWithPassword()
                .name("test")
                .email("test@test.com")
                .role(ROLE.user)
                .loginProvider(LoginProvider.original)
                .contact("010-1234-5678")
                .password("test")
                .builderWithPassword();
    }

    protected Member createTestMemberForEvent() {
        return Member.builderWithPassword()
                .name("testMember")
                .email("testMember@test.com")
                .role(ROLE.user)
                .loginProvider(LoginProvider.original)
                .contact("010-1234-5678")
                .password("test123131231")
                .builderWithPassword();
    }

    protected Event createTestEvent(Member member) {
        return Event.builder()
                .eventInfo(
                        EventInfo.builder()
                                .isType(true)
                                .isHistory(true)
                                .isPrice(true)
                                .isName(true)
                                .isTag(true)
                                .isImage(true)
                                .isSide(true)
                                .isSend(true)
                                .build()
                )
                .eventDate(LocalDate.now())
                .eventName("test")
                .eventType("test")
                .participants(100)
                .location(
                        Location.builder()
                                .address("test")
                                .latitude(123.123)
                                .longitude(456.456)
                                .build()
                )
                .member(member)
                .build();
    }


    protected EventDetail createTestEventDetail(
            Event event,
            Target target
    ) {
        return EventDetail.builder()
                .price(String.valueOf(1234))
                .event(event)
                .history("sdfsd")
                .image("image")
                .type("type")
                .target(target)
                .contact("010-1111-2222")
                .name("name")
                .build();
    }

    protected Target createTestTarget() {
        return Target.builder()
                .value("target")
                .build();
    }

    protected Tag createTestTag(EventDetail eventDetail) {
        return Tag.builder()
                .value("Tag")
                .build();
    }

}
