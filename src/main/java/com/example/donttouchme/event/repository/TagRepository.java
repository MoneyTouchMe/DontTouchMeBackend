package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
