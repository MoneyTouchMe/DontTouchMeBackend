package com.example.donttouchme.event.controller.dto;

import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.event.domain.value.SendType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEventRequest(
        @NotBlank(message = "이벤트명은 필수입니다.")
        String eventName,

        @NotBlank(message = "이벤트 유형은 필수입니다.")
        String eventType,

        @NotBlank(message = "이벤트 일정은 필수입니다.")
        LocalDate eventDate,

        @NotBlank(message = "이벤트 장소는 필수입니다.")
        String address,

        Integer participants,

        boolean isType,

        boolean isHistory,

        boolean isPrice,

        boolean isName,

        boolean isTag,

        boolean isImage,

        boolean isSide,

        boolean isSend,

        SendType sendType

) {
    @AssertTrue(message = "감사장 전송 여부가 true일 때 감사장 종류를 지정해야합니다.")
    public boolean isSendTypeValid() {
        if (isSend()) {
            return sendType != null;
        }
        return true;
    }

}
