package com.example.donttouchme.image.controller.dto;

import jakarta.validation.constraints.NotNull;

public record UploadImageRequest(
        @NotNull(message = "eventId는 필수입니다.")
        Long eventId
) {
}
