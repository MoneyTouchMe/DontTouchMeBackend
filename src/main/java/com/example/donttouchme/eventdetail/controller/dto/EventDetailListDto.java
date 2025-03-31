package com.example.donttouchme.eventdetail.controller.dto;

public record EventDetailListDto(
        Long eventDetailId,
        String type,
        String history,
        String price,
        String name,
        String image,
        String contact
) {
}
