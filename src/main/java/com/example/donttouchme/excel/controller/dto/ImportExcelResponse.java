package com.example.donttouchme.excel.controller.dto;

import com.example.donttouchme.event.domain.EventDetail;

import java.util.List;

public record ImportExcelResponse(
        List<Long> eventDetailIds
) {
        public static ImportExcelResponse of(List<EventDetail> eventDetails) {
            return new ImportExcelResponse(
                    eventDetails.stream().map(EventDetail::getId).toList()
            );
        }
}
