package com.example.donttouchme.excel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "excel 관련 API", description = "excel 관련 API 입니다.")
public interface ExcelControllerSagger {

    @Operation(
            summary = "엑셀 양식 다운로드 API",
            description = "엑셀 양식을 다운로드 합니다."
    )
    ResponseEntity<byte[]> exportSampleExcel(
            Long eventId
    );
}
