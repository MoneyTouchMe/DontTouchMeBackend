package com.example.donttouchme.eventdetail.controller.dto;

import com.example.donttouchme.event.domain.value.SendType;
import com.example.donttouchme.eventdetail.controller.dto.validation.ContactValidatable;
import com.example.donttouchme.eventdetail.controller.dto.validation.ValidContactAndSendType;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@ValidContactAndSendType
public record UpdateEventDetailRequest(
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
    @Override
    public String contact() {
        return contact;
    }

    @Override
    public SendType sendType() {
        return sendType;
    }
}
