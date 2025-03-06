package com.example.donttouchme.image;

import com.example.donttouchme.image.controller.dto.UploadImageResponse;
import com.example.donttouchme.image.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ImageServiceTest {
    @Autowired
    ImageService imageService;

    @Test
    @DisplayName("Presigned Url 요청 성공")
    void getPresignedUrlSuccess() {
        UploadImageResponse response = imageService.getPresignedUrl("myImage.jpg");

        Assertions.assertThat(response).isNotNull();
        log.info("presignedUrl: {}, fileUrl: {}", response.presignedUrl(), response.fileUrl());
    }
}
