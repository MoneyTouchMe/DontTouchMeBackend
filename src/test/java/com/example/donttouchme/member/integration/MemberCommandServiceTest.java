package com.example.donttouchme.member.integration;

import com.example.donttouchme.member.controller.dto.ChangePasswordRequest;
import com.example.donttouchme.member.controller.dto.MemberSignUpRequest;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.member.service.MemberCommandService;
import com.example.donttouchme.member.service.dto.CreateMemberDto;
import com.example.donttouchme.support.IntegrationTestSupport;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
class MemberCommandServiceTest extends IntegrationTestSupport {
    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("OAuth2 member 정상 생성")
    void OAuth2memberCreateSuccess() {

        //given
        CreateMemberDto createMemberDto = new CreateMemberDto(
                "test",
                "test@test.com",
                ROLE.user,
                LoginProvider.original
        );

        //when
        Member member = memberCommandService.createMember(createMemberDto);

        //then
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("OAuth2 member 생성 실패")
    void OAuth2memberCreateFail() {

        //given
        CreateMemberDto createMemberDto = new CreateMemberDto(
                null,
                "test@test.com",
                ROLE.user,
                LoginProvider.google
        );

        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.createMember(createMemberDto);
        });
    }

    @Test
    @DisplayName("original member 생성 성공")
    void originalMemberCreateSuccess() {

        //given
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(
                "test",
                "test@Test.com",
                "test",
                "test"
        );

        //when
        Member member = memberCommandService.signUp(memberSignUpRequest);

        //then
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("original member 생성 실패")
    void originalMemberCreateFail() {

        //given
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(
                null,
                "test@Test.com",
                "test",
                "test"
        );


        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.signUp(memberSignUpRequest);
        });
    }

    @Test
    @DisplayName("이메일 중복 검사 실패로 member 생성실패")
    void memberCreateFailedByDuplicate() {

        //given
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(
                "test",
                "test@Test.com",
                "test",
                "test"
        );
        Member member = memberCommandService.signUp(memberSignUpRequest);


        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.signUp(memberSignUpRequest);
        });
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePasswordSuccess() {
        // given
        Member testMember = createTestMember();
        Member save = memberRepository.save(testMember);
        String originalPassword = save.getPassword();
        // when
        memberCommandService.changePassword(
                save,
                new ChangePasswordRequest("zzzzz")
        );

        // then
        assertThat(save.getPassword()).isNotEqualTo(originalPassword);
    }

    @Test
    @DisplayName("비빌번호 변경 예외")
    void changePasswordFail() {
        //given
        Member testMember = createTestMember();
        Member save = memberRepository.save(testMember);

        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberCommandService.changePassword(
                    save,
                    new ChangePasswordRequest(null)
            );
        });
    }

}