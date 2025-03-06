package com.example.donttouchme.map;

import com.example.donttouchme.map.controller.dto.GeocodeResponse;
import com.example.donttouchme.map.service.MapService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapServiceTest {
    @Autowired
    private MapService mapService;

    @Test
    @DisplayName("주소를 위도, 경도로 변환 성공")
    void getCoordinateSuccess() {
        //given
        GeocodeResponse response = mapService.getCoordinate("경기도 안양시 동안구 흥안대로 지하 529");

        //when & then
        Assertions.assertThat(response).extracting("latitude", "longitude", "address")
                .containsExactly(37.4015080484355, 126.976688773261, "경기도 안양시 동안구 흥안대로 지하 529");
    }
}
