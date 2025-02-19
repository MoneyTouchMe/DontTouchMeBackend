package com.example.donttouchme.common.user.service;

import com.example.donttouchme.common.user.domain.User;
import com.example.donttouchme.common.user.repository.UserRepository;
import com.example.donttouchme.common.user.service.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;

    public User createUser(final UserSignUpDto userSignUpDto) {
        return userRepository.save(User.builder()
                .email(userSignUpDto.email())
                .name(userSignUpDto.name())
                .username(userSignUpDto.username())
                .role(userSignUpDto.role())
                .build());
    }
}
