package com.example.donttouchme.map.controller;

import com.example.donttouchme.map.service.MapService;
import com.example.donttouchme.map.controller.dto.GeocodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    //주소를 위도, 경도로 변환
    @GetMapping("/geocode")
    public GeocodeResponse getCoordinate(
            @RequestParam final String address
    ) {
        return mapService.getCoordinate(address);
    }
}
