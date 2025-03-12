package com.example.donttouchme.event.controller.dto;

import com.example.donttouchme.event.domain.value.SendType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record UpdateEventRequest(
        String thumbnailUrl, //이미지 URL

        @NotBlank(message = "이벤트명은 필수입니다.")
        String eventName,

        @NotBlank(message = "이벤트 유형은 필수입니다.")
        String eventType,

        @NotNull(message = "이벤트 일정은 필수입니다.")
        LocalDate eventDate,

        @NotBlank(message = "이벤트 장소는 필수입니다.")
        String address,

        @NotNull(message = "이벤트 장소 위도 정보는 필수입니다.")
        double latitude,

        @NotNull(message = "이벤트 장소 경도 정보는 필수입니다.")
        double longitude,

        Integer participants,

        boolean isType, //입출금 분류 여부

        boolean isHistory, //입출금 내역명 여부

        boolean isPrice, //금액 태그화 여부

        boolean isName, //이름 입력 여부

        List<String> tags, //태그 (토글이 OFF일 때 null)

        boolean isImage, //사진 첨부 여부

        List<String> targets, //입금 대상 (토글이 OFF일 때 null)

        boolean isSend, //감사장 여부

        SendType sendType //감사장 타입

) {
    @AssertTrue(message = "감사장 전송 여부가 true일 때 감사장 종류를 지정해야합니다.")
    public boolean isSendTypeValid() {
        if (isSend()) {
            return sendType != null;
        }
        return true;
    }
}

