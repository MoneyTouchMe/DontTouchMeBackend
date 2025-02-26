package com.example.donttouchme.excel.service;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.EventDetail;
import com.example.donttouchme.event.domain.Tag;
import com.example.donttouchme.event.domain.Target;
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
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 사용
            EventInfo eventInfo = event.getEventInfo();

            // 헤더 매핑 생성
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headerMap = createHeaderMap(headerRow, eventInfo);
            List<EventDetail> eventDetails = new ArrayList<>();
            // 데이터 행 처리
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // 빈 행은 건너뛰기

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
            if (eventInfo.isSide() && "입금대상".equalsIgnoreCase(header)) headerMap.put("target", i); // Target을 Side로 매핑 가정
        }
        return headerMap;
    }

    private EventDetail buildEventDetailFromRow(Row row, Map<String, Integer> headerMap, EventInfo eventInfo, Event event) {
        EventDetail.EventDetailBuilder builder = EventDetail.builder().event(event);

        // 각 필드 설정
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
        if (eventInfo.isTag() && headerMap.containsKey("tags")) {
            String tagsValue = getCellValue(row, headerMap.get("tags"));
            if (tagsValue != null && !tagsValue.isEmpty()) {
                List<Tag> tags = Arrays.stream(tagsValue.split(","))
                        .map(String::trim)
                        .map(value -> Tag.builder().value(value).eventDetail(builder.build()).build()) // 임시 빌드 필요
                        .toList();
                // tags는 빌더로 바로 설정 불가, 이후 처리
            }
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

        // Tag는 EventDetail이 완성된 후 추가
        if (eventInfo.isTag() && headerMap.containsKey("tags")) {
            String tagsValue = getCellValue(row, headerMap.get("tags"));
            if (tagsValue != null && !tagsValue.isEmpty()) {
                List<Tag> tags = Arrays.stream(tagsValue.split(","))
                        .map(String::trim)
                        .map(value -> Tag.builder().value(value).eventDetail(detail).build())
                        .toList();
                detail.getTags().addAll(tags);
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
