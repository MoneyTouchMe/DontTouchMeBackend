package com.example.donttouchme.eventdetail.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateEventDetailRequest(
        @NotNull(message = "이벤트 고유번호는 필수입니다.")
        long eventId,

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

        String contact
) {
}
