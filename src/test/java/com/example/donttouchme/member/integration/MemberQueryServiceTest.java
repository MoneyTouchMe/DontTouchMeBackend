package com.example.donttouchme.member.integration;

import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.member.service.MemberQueryService;
import com.example.donttouchme.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Slf4j
class MemberQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("email과 loginProvicer로 member 검색")
    void findMemberByEmailAndProviderTest() {
        Member testMember = createTestMember();

        memberRepository.save(testMember);


        Optional<Member> memberByEmailAndProvider = memberQueryService.findMemberByEmailAndProvider(testMember.getEmail(), testMember.getLoginProvider());

        assertThat(memberByEmailAndProvider).isPresent();
    }

    @Test
    @DisplayName("email 중복 검사")
    void checkDuplicateEmailTest() {
        Member testMember = createTestMember();

        memberRepository.save(testMember);

        assertThat(
                memberQueryService.checkDuplicateEmail(testMember.getEmail())
        ).isTrue();
    }
}