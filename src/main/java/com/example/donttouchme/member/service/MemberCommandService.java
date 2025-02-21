package com.example.donttouchme.member.service;

import com.example.donttouchme.member.controller.dto.MemberSignUpRequest;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.domain.value.ROLE;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.member.service.dto.CreateMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberCommandService {

    public final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member createMember(final CreateMemberDto createMemberDto) {
        try {
            return memberRepository.save(Member.builderWithoutPassword()
                    .email(createMemberDto.email())
                    .name(createMemberDto.name())
                    .loginProvider(createMemberDto.loginProvider())
                    .role(createMemberDto.role())
                    .builderWithoutPassword()
            );
        }catch (Exception e) {
            throw new IllegalArgumentException("회원 생성 실패");
        }

    }

    public Member signUp(final MemberSignUpRequest request) {
        Member member = Member.builderWithPassword()
                .email(request.email())
                .password(bCryptPasswordEncoder.encode(request.password()))
                .name(request.name())
                .role(ROLE.user)
                .loginProvider(LoginProvider.original)
                .builderWithPassword();
        log.info("encoded password: {}",bCryptPasswordEncoder.encode(request.password()));
        log.info("password : {}", member.getPassword());

        try {
            return memberRepository.save(
                    member
            );
        }catch (Exception e) {

            throw new IllegalArgumentException("회원 생성 실패");
        }

    }
}
