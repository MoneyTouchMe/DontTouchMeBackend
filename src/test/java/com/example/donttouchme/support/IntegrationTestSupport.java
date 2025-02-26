package com.example.donttouchme.support;


import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

}
