package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.domain.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Optional<Target> findByValue(String value);
}
