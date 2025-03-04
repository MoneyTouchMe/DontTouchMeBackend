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

    //업로드용 presignedUrl 제공 (PUT 요청으로 파일 업로드)
    @GetMapping("/upload-url")
    public UploadImageResponse getUploadUrl(@RequestParam String fileName) {
        return imageService.generatePresignedUrl(fileName);
    }

    /*//다운로드 presignedURL 요청 (GET 요청으로 파일 다운로드 가능)
    @GetMapping("/download-url")
    public UploadImageResponse getDownloadUrl(@RequestParam String fileName) {
        return imageService.getDownloadPresignedUrl(fileName);
    }*/

    /*//이미지 URL 반환 (public 버킷)
    @GetMapping("/public-url")
    public String getPublicUrl(@RequestParam String fileName) {
        return imageService.getPublicImageUrl(fileName);
    }*/
}
