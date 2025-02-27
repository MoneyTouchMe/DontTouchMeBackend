package com.example.donttouchme.event.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Location { //이벤트 장소

    @Column(nullable = false)
    private BigDecimal latitude; //위도

    @Column(nullable = false)
    private BigDecimal longitude; //경도

    @Column(nullable = false)
    private String address; //주소

    @Builder
    public Location(BigDecimal latitude, BigDecimal longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
