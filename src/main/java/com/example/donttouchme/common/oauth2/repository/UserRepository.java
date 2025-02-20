package com.example.donttouchme.common.oauth2.repository;

import com.example.donttouchme.common.oauth2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
