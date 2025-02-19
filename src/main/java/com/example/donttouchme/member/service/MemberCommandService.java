package com.example.donttouchme.member.service;

import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.repository.MemberRepository;
import com.example.donttouchme.member.service.dto.CreateMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    public final MemberRepository memberRepository;

    public Member createMember(final CreateMemberDto createMemberDto) {
        return memberRepository.save(Member.builder()
                .email(createMemberDto.email())
                .name(createMemberDto.name())
                .loginProvider(createMemberDto.loginProvider())
                .role(createMemberDto.role())
                .build()
        );
    }
}
