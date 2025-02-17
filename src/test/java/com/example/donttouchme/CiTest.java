package com.example.donttouchme;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CiTest {

    @Test
    @DisplayName("테스트 성공")
    void test() {
        assertThat(true).isTrue();
    }

    @Test
    @DisplayName("테스트 실패")
    void failTest(){
        assertThat(false).isTrue();
    }
}
