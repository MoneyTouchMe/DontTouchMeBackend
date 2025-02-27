package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.domain.Target;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepository extends JpaRepository<Target, Long> {
}
