package com.example.donttouchme.common.jwt.repository;

import com.example.donttouchme.common.jwt.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
