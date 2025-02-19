package com.example.donttouchme.member.repository;

import com.example.donttouchme.member.domain.Member;
import com.example.donttouchme.member.domain.value.LoginProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmailAndLoginProvider(String email, LoginProvider loginProvider);
}
