package com.example.donttouchme.tag.repository;

import com.example.donttouchme.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
