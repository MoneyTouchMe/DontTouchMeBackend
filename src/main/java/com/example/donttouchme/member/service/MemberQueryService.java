package com.example.donttouchme.member.service;

import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public boolean existsMemberByEmailAndProvider(final String email, final LoginProvider provider) {
        return memberRepository.existsByEmailAndLoginProvider(email, provider);
    }
}
