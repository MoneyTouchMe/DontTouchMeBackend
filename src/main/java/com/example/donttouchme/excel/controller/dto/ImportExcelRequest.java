package com.example.donttouchme.excel.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record ImportExcelRequest(
        @NotEmpty(message = "eventId는 필수입니다.")
        Long eventId
) {
}
