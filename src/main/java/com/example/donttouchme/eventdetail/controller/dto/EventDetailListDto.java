package com.example.donttouchme.eventdetail.controller.dto;

import java.util.List;

public record EventDetailListDto(
        Long eventDetailId,
        String type,
        String history,
        String price,
        String name,
        String image,
        String contact,
        String target,
        List<String> tags
) {
}
