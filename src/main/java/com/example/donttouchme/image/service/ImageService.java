package com.example.donttouchme.image.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.donttouchme.image.controller.dto.UploadImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class ImageService {
    private final AmazonS3 amazonS3;
    private final String bucketName;

    public ImageService(AmazonS3 amazonS3, @Value("${aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    //presignedUrl 생성
    public UploadImageResponse getPresignedUrl(final String fileName) {
        String uniqueFileName = generateUniqueFileName(fileName);

        // 만료 시간 설정 (10분)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + 1000 * 60 * 10;
        expiration.setTime(expTimeMillis);

        //presignedUrl 요청 생성 (PUT 방식으로 업로드)
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, uniqueFileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        // Presigned URL 반환
        URL presignedUrl = amazonS3.generatePresignedUrl(request); //클라이언트에서 업로드할 URL
        String fileUrl = extractFileUrl(uniqueFileName); // 최종 저장될 S3 URL

        return new UploadImageResponse(presignedUrl.toString(), fileUrl);
    }

    //파일명에 UUID를 추가해서 고유한 파일명으로 변경
    private String generateUniqueFileName(String fileName) {
        String fileExtension = "";
        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex != -1) {
            fileExtension = fileName.substring(dotIndex); //파일의 확장자
            fileName = fileName.substring(0, dotIndex); // 확장자 제외한 파일명
        }
        return fileName + "-" + UUID.randomUUID() + fileExtension;
    }

    //S3 최종 이미지 URL 생성
    public String extractFileUrl(final String fileName) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
    }
}
