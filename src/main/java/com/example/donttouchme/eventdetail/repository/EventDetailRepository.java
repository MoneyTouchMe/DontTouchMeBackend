package com.example.donttouchme.eventdetail.repository;

import com.example.donttouchme.eventdetail.domain.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDetailRepository extends JpaRepository<EventDetail, Long>, EventDetailRepositoryCustom {
}
