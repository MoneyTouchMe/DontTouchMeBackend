package com.example.donttouchme.image.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record UploadImageRequest(
        @NotEmpty(message = "eventId는 필수입니다.")
        Long eventId
) {
}
