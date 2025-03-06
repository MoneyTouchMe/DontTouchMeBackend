package com.example.donttouchme.image.controller;

import com.example.donttouchme.image.controller.dto.UploadImageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Image 관련 API", description = "Image 관련 API")
public interface ImageControllerSwagger {
    @Operation(
            summary = "이미지 업로드용 presignedUrl 요청 API",
            description = "이미지 업로드용 presignedUrl을 요청합니다.",
            responses = {
                    @ApiResponse(
                            content = @Content(
                                    schema = @Schema(implementation = UploadImageResponse.class)
                            )
                    )
            }
    )
    public UploadImageResponse getPresignedUrl(
            @RequestParam final String fileName
    );
}
