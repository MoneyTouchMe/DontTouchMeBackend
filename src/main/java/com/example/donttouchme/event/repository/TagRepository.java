package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByValueAndEventId(String value, Long eventId);
}
