package com.example.donttouchme.support;


import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public abstract class IntegrationTestSupport {

    protected Member createTestMember(){
        return Member.builderWithPassword()
                .name("test")
                .email("test@test.com")
                .role(ROLE.user)
                .loginProvider(LoginProvider.original)
                .contact("010-1234-5678")
                .password("test")
                .builderWithPassword();
    }

    protected Event createTestEvent(Member member){
        return Event.builder()
                .eventInfo(
                        EventInfo.builder()
                                .isType(true)
                                .isTag(true)
                                .isSide(true)
                                .isHistory(true)
                                .isImage(true)
                                .isName(true)
                                .isPrice(true)
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
                                .latitude(new BigDecimal(123))
                                .longitude(new BigDecimal(456))
                                .build()
                )
                .member(member)
                .build();
    }

}
