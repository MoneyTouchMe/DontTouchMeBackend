package com.example.donttouchme.image.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "presignedUrl 요청 응답 객체 모델")
public record UploadImageResponse(
        @Schema(description = "이미지 업로드용 presignedUrl")
        String presignedUrl,
        @Schema(description = "S3에 최종 저장되는 URL")
        String fileUrl
) {
}
