package com.example.donttouchme.image.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record UploadImageResponse(
        String fileUrl
) {
}
