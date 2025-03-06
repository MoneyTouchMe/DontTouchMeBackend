package com.example.donttouchme.map.service;

import com.example.donttouchme.map.controller.dto.GeocodeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapService {
    @Value("${kakao.api.key}")
    private String kakaoApiKey;
    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";

    //카카오 지도 API를 사용해서 주소로 위도 경도 변환
    public GeocodeResponse getCoordinate(final String address) {
        RestTemplate restTemplate = new RestTemplate();

        //HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //API 호출
        String url = KAKAO_API_URL + address;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        //JSON 응답 파싱
        String responseBody = response.getBody();

        if (responseBody.isEmpty()) {
            throw new RuntimeException("API 응답이 비어 있습니다.");
        }

        if (!responseBody.startsWith("{")) {
            throw new RuntimeException("API 응답이 JSON 형식이 아닙니다: " + responseBody);
        }

        try {
            // ObjectMapper를 사용하여 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);

            // documents 배열 확인
            JsonNode documents = root.path("documents");
            if (documents.isEmpty()) {
                throw new IllegalArgumentException("주소를 찾을 수 없습니다.");
            }

            //위도, 경도 추출
            JsonNode location = documents.get(0);
            double latitude = location.path("y").asDouble();
            double longitude = location.path("x").asDouble();

            return new GeocodeResponse(address, latitude, longitude);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }

    }
}
