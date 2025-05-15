package com.example.donttouchme.member.service;

import com.example.donttouchme.member.controller.dto.CheckCurrentPasswordRequest;
import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<Member> findMemberByEmailAndProvider(final String email, final LoginProvider provider) {
        return memberRepository.findByEmailAndLoginProvider(email, provider);
    }

    public boolean checkDuplicateEmail(final String email) {
        return memberRepository.existsByEmailAndLoginProvider(email, LoginProvider.original);
    }

    public boolean checkCurrentPassword(
            final Member member,
            final CheckCurrentPasswordRequest request
    ) {
        return bCryptPasswordEncoder.matches(request.currentPassword(), member.getPassword());
    }
}
