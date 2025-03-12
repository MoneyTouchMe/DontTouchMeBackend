package com.example.donttouchme.event.controller.dto;

import jakarta.validation.constraints.NotNull;

public record FindEventListRequest(
        @NotNull(message = "회원 고유번호는 필수입니다.")
        Long memberId,

        Long lastEventId, //이전 페이지의 마지막 데이터 ID

        @NotNull(message = "페이지 사이즈는 필수입니다.")
        int pageSize //한 번에 가져올 데이터 개수
) {
}
