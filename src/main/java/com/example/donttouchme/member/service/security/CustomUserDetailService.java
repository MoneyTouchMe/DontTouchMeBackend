package com.example.donttouchme.member.service.security;

import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import com.example.donttouchme.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmailAndLoginProvider(username, LoginProvider.original).orElseThrow(
                () -> new IllegalArgumentException("해당하는 Member가 없습니다.")
        );

        if (member != null) {
            return new CustomUserDetails(member);
        }
        return null;
    }
}
