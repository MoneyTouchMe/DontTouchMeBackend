package com.example.donttouchme.common.user.service;

import com.example.donttouchme.common.OAuth2.dto.UserDto;
import com.example.donttouchme.common.user.domain.User;
import com.example.donttouchme.common.user.repository.UserRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;

    public User createUser(final UserDto userDto) {
        return userRepository.save(User.builder()
                .email(userDto.email())
                .name(userDto.name())
                .username(userDto.username())
                .build());
    }
}
