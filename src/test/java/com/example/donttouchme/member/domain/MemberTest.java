package com.example.donttouchme.member.domain;

import com.example.donttouchme.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest extends IntegrationTestSupport {

    @Test
    @DisplayName("member password 변경 테스트")
    void passwordChangeTest() {

        //given
        Member testMember = createTestMember();

        //when
        testMember.changePassword("1234");

        //then
        assertThat(testMember.getPassword()).isEqualTo("1234");
    }
}
