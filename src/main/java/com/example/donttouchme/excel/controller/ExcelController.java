package com.example.donttouchme.excel.controller;

import com.example.donttouchme.excel.service.ExcelQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/excel")
@RequiredArgsConstructor
public class ExcelController implements ExcelControllerSagger {

    private final ExcelQueryService excelQueryService;

    @Override
    @GetMapping("/download")
    public ResponseEntity<byte[]> exportSampleExcel(
            @RequestParam final Long eventId
    ){
        byte[] excelByte = excelQueryService.exportSampleExcel(eventId);

        HttpHeaders headers = setHeader("event_form.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelByte);
    }

    @Override
    @GetMapping()
    public ResponseEntity<byte[]> exportEventToExcel(
            @RequestParam final Long eventId) {
        byte[] excelByte = excelQueryService.exportEventToExcel(eventId);

        HttpHeaders headers = setHeader("event.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelByte);
    }

    private HttpHeaders setHeader(final String fileName) {
        HttpHeaders headers = new HttpHeaders();

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return headers;
    }
}
