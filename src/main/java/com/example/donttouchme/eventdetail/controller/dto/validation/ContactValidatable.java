package com.example.donttouchme.eventdetail.controller.dto.validation;

import com.example.donttouchme.event.domain.value.SendType;

public interface ContactValidatable {
    String contact();

    SendType sendType();
}
