package com.example.donttouchme.excel.integration;

import com.example.donttouchme.event.domain.*;
import com.example.donttouchme.event.repository.EventDetailRepository;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.repository.TagRepository;
import com.example.donttouchme.excel.service.ExcelQueryService;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ExcelQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    ExcelQueryService excelQueryService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventDetailRepository eventDetailRepository;

    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("엑셀양식 생성 성공")
    void exportSampleExcelSuccess() {

        //given
        Member member = memberRepository.save(createTestMember());
        Event event = eventRepository.save(createTestEvent(member));

        //when
        byte[] bytes = excelQueryService.exportSampleExcel(event.getId());

        //then
        assertThat(bytes).isNotNull();
    }

    @Test
    @DisplayName("엑셀로 추출 성공")
    void exportToExcelSuccess() {

        //given
        Member member = memberRepository.save(createTestMember());
        Event event = eventRepository.save(createTestEvent(member));
        Target target = createTestTarget();
        SendValue sendValue = createTestSendValue();
        EventDetail eventDetail = eventDetailRepository.save(createTestEventDetail(event, target, sendValue));
        Tag tag = tagRepository.save(createTestTag(eventDetail));

        //then
        byte[] bytes = excelQueryService.exportEventToExcel(event.getId());

        //then
        assertThat(bytes).isNotNull();
    }
}