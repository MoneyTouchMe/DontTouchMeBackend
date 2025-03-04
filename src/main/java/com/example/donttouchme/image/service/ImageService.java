package com.example.donttouchme.image.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.donttouchme.image.controller.dto.UploadImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
public class ImageService {
    private final AmazonS3 amazonS3;
    private final String bucketName;

    public ImageService(AmazonS3 amazonS3, @Value("${aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    //presignedUrl 생성 (PUT 방식)
    public UploadImageResponse generatePresignedUrl(String fileName) {
        // 만료 시간 설정 (10분)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + 1000 * 60 * 10;
        expiration.setTime(expTimeMillis);

        //presignedUrl 요청 생성
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        // Presigned URL 반환
        URL presignedUrl = amazonS3.generatePresignedUrl(request);
        String fileUrl = getFileUrl(fileName); // 최종 저장될 S3 URL

        return new UploadImageResponse(presignedUrl.toString(), fileUrl);
    }

    //S3 최종 이미지 URL 생성
    public String getFileUrl(String fileName) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
    }
}
