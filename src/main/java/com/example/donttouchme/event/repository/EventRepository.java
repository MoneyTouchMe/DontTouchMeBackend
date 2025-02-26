package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.domain.Event;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "LEFT JOIN FETCH e.eventDetails ed " +
            "LEFT JOIN FETCH ed.target " +
            "LEFT JOIN FETCH ed.sendValue " +
            "LEFT JOIN FETCH ed.tags " +
            "WHERE e.id = :eventId AND e.deletedAt IS NULL")
    Optional<Event> findByIdWithDetails(@Param("eventId") Long eventId);
}
