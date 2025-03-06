package com.example.donttouchme.image.controller;

import com.example.donttouchme.image.controller.dto.UploadImageResponse;
import com.example.donttouchme.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    //이미지 업로드용 presignedUrl 요청
    @GetMapping("/upload")
    public UploadImageResponse getPresignedUrl(
            @RequestParam final String fileName
    ) {
        return imageService.getPresignedUrl(fileName);
    }
}
