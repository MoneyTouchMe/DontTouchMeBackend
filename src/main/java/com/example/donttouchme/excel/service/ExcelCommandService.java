package com.example.donttouchme.excel.service;

import com.example.donttouchme.event.domain.*;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.repository.EventDetailRepository;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.event.repository.TagRepository;
import com.example.donttouchme.event.repository.TargetRepository;
import com.example.donttouchme.excel.controller.dto.ImportExcelResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelCommandService {

    private final EventRepository eventRepository;
    private final EventDetailRepository eventDetailRepository;
    private final TargetRepository targetRepository;
    private final TagRepository tagRepository;

    public ImportExcelResponse importEventDetailsFromExcel(
            final Long eventId,
            final byte[] excelData
    ) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 하는 event가 없습니다."));

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelData))) {
            Sheet sheet = workbook.getSheetAt(0);
            EventInfo eventInfo = event.getEventInfo();

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headerMap = createHeaderMap(headerRow, eventInfo);
            List<EventDetail> eventDetails = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                EventDetail eventDetail = buildEventDetailFromRow(row, headerMap, eventInfo, event);
                eventDetails.add(eventDetailRepository.save(eventDetail));
            }

            return ImportExcelResponse.of(eventDetails);
        } catch (IOException e) {
            throw new IllegalArgumentException("엑셀 파일 처리 실패", e);
        }
    }

    private Map<String, Integer> createHeaderMap(
            final Row headerRow,
            final EventInfo eventInfo
    ) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String header = headerRow.getCell(i).getStringCellValue().trim();
            if (eventInfo.isType() && "입출금 분류".equalsIgnoreCase(header)) headerMap.put("type", i);
            if (eventInfo.isHistory() && "입출금 내역명".equalsIgnoreCase(header)) headerMap.put("history", i);
            if (eventInfo.isPrice() && "금액".equalsIgnoreCase(header)) headerMap.put("price", i);
            if (eventInfo.isName() && "이름".equalsIgnoreCase(header)) headerMap.put("name", i);
            if (eventInfo.isTag() && "태그".equalsIgnoreCase(header)) headerMap.put("tags", i);
            if (eventInfo.isSide() && "입금대상".equalsIgnoreCase(header)) headerMap.put("target", i);
        }
        return headerMap;
    }

    private EventDetail buildEventDetailFromRow(Row row, Map<String, Integer> headerMap, EventInfo eventInfo, Event event) {
        EventDetail.EventDetailBuilder builder = EventDetail.builder().event(event);

        if (eventInfo.isType() && headerMap.containsKey("type")) {
            builder.type(getCellValue(row, headerMap.get("type")));
        }
        if (eventInfo.isHistory() && headerMap.containsKey("history")) {
            builder.history(getCellValue(row, headerMap.get("history")));
        }
        if (eventInfo.isPrice() && headerMap.containsKey("price")) {
            builder.price(getCellValue(row, headerMap.get("price")));
        }
        if (eventInfo.isName() && headerMap.containsKey("name")) {
            builder.name(getCellValue(row, headerMap.get("name")));
        }
        if (eventInfo.isSide() && headerMap.containsKey("target")) {
            String targetValue = getCellValue(row, headerMap.get("target"));
            if (targetValue != null && !targetValue.isEmpty()) {
                Target target = Target.builder().value(targetValue).build();
                targetRepository.save(target);
                builder.target(target);
            }
        }

        EventDetail detail = builder.build();

        // Tag 처리
        if (eventInfo.isTag() && headerMap.containsKey("tags")) {
            String tagsValue = getCellValue(row, headerMap.get("tags"));
            if (tagsValue != null && !tagsValue.isEmpty()) {
                List<TagEventDetail> tagEventDetails = Arrays.stream(tagsValue.split(","))
                        .map(String::trim)
                        .map(value -> {
                            Tag tag = Tag.builder()
                                    .value(value)
                                    .event(event)
                                    .build();
                            tagRepository.save(tag);

                            return TagEventDetail.builder()
                                    .eventDetail(detail)
                                    .tag(tag)
                                    .build();
                        })
                        .toList();
                detail.getTags().addAll(tagEventDetails);
            }
        }

        return detail;
    }

    private String getCellValue(Row row, Integer colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BLANK -> null;
            default -> null;
        };
    }
}