package com.example.donttouchme.member.service;

import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Optional<Member> findMemberByEmailAndProvider(final String email, final LoginProvider provider) {
        return memberRepository.findByEmailAndLoginProvider(email, provider);
    }

    public boolean checkDuplicateEmail(final String email) {
        return memberRepository.existsByEmailAndLoginProvider(email, LoginProvider.original);
    }
}
