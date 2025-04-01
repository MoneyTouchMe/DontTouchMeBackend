package com.example.donttouchme.eventdetail.controller.dto;

import com.example.donttouchme.event.domain.value.SendType;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record FindEventDetailResponse(
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
) {
    public static FindEventDetailResponse from(EventDetail eventDetail, List<String> tags, String target) {
        return new FindEventDetailResponse(
                eventDetail.getType(),
                eventDetail.getHistory(),
                eventDetail.getPrice(),
                eventDetail.getName(),
                tags,
                eventDetail.getImage(),
                target,
                eventDetail.getEvent().getSendType(),
                eventDetail.getContact()
        );
    }
}
