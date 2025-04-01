package com.example.donttouchme.eventdetail.controller.dto;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.event.domain.value.SendType;
import com.example.donttouchme.eventdetail.controller.dto.validation.ContactValidatable;
import com.example.donttouchme.eventdetail.controller.dto.validation.ValidContactAndSendType;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@ValidContactAndSendType
public record CreateEventDetailRequest(
        @NotNull(message = "이벤트 고유번호는 필수입니다.")
        Long eventId,

        @NotBlank(message = "입출금 분류 항목은 필수입니다.")
        String type,

        @NotBlank(message = "입출금 내역명은 필수입니다.")
        String history,

        @NotBlank(message = "금액은 필수입니다.")
        String price,

        String name,

        List<String> tags,

        String imageUrl,

        String target,

        SendType sendType,

        String contact
) implements ContactValidatable {
    public EventDetail toEntity(
            final Event event,
            final Target target
    ) {
        return EventDetail.builder()
                .type(type)
                .history(history)
                .price(price)
                .name(name)
                .image(imageUrl)
                .contact(contact)
                .event(event)
                .target(target)
                .build();
    }

    @Override
    public String contact() {
        return contact;
    }

    @Override
    public SendType sendType() {
        return sendType;
    }
}
