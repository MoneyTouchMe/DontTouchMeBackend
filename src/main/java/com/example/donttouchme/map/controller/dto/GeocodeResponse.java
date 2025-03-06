package com.example.donttouchme.map.controller.dto;

public record GeocodeResponse(
        String address,
        double latitude,
        double longitude
) {
}
