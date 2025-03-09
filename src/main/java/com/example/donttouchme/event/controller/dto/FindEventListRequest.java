package com.example.donttouchme.event.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record FindEventListRequest(
        @NotBlank(message = "회원 고유번호는 필수입니다.")
        Long memberId,

        Long lastEventId, //이전 페이지의 마지막 데이터 ID

        @NotBlank(message = "페이지")
        int pageSize //한 번에 가져올 데이터 개수
) {
}
