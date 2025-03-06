package com.example.donttouchme.image.controller.dto;

public record UploadImageResponse(
        String presignedUrl, //업로드용 presignedUrl
        String fileUrl //최종 저장되는 URL
) {
}
