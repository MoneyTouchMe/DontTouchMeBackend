package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.domain.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDetailRepository extends JpaRepository<EventDetail,Long> {
}
