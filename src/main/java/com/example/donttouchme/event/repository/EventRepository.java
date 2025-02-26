package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
