package com.example.donttouchme.event.service;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Event createEvent() {
        return null;
    }
}
