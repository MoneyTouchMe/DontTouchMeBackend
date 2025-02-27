package com.example.donttouchme.excel.service;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.EventDetail;
import com.example.donttouchme.event.domain.Tag;
import com.example.donttouchme.event.domain.TagEventDetail;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelQueryService {

    private final EventRepository eventRepository;

    public byte[] exportSampleExcel(final Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 하는 event가 없습니다."));

        try (
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            createHeader(event, workbook);

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("엑셀양식 추출 실패");
        }
    }

    public byte[] exportEventToExcel(final Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 하는 event가 없습니다."));

        try (
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = createHeader(event, workbook);

            List<EventDetail> eventDetails = event.getEventDetails();
            EventInfo eventInfo = event.getEventInfo();
            int rowNum = 1;

            for (EventDetail detail : eventDetails) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;

                if (eventInfo.isType()) {
                    row.createCell(colNum++).setCellValue(detail.getType() != null ? detail.getType() : "");
                }
                if (eventInfo.isHistory()) {
                    row.createCell(colNum++).setCellValue(detail.getHistory() != null ? detail.getHistory() : "");
                }
                if (eventInfo.isPrice()) {
                    row.createCell(colNum++).setCellValue(detail.getPrice() != null ? detail.getPrice() : "");
                }
                if (eventInfo.isName()) {
                    row.createCell(colNum++).setCellValue(detail.getName() != null ? detail.getName() : "");
                }
                if (eventInfo.isTag()) {
                    String tagValues = detail.getTags().stream()
                            .map(TagEventDetail::getTag)
                            .map(Tag::getValue)
                            .filter(value -> value != null && !value.isEmpty())
                            .collect(Collectors.joining(", "));
                    row.createCell(colNum++).setCellValue(tagValues);
                }
                if (eventInfo.isSide()) {
                    row.createCell(colNum++).setCellValue(
                            detail.getTarget() != null && detail.getTarget().getValue() != null
                                    ? detail.getTarget().getValue()
                                    : ""
                    );
                }
            }

            // 열 너비 자동 조정
            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("엑셀양식 추출 실패");
        }
    }

    private Sheet createHeader(final Event event, final XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet(event.getEventName());
        List<String> cellValues = event.getEventInfo().toCellValues();
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < cellValues.size(); i++) {
            headerRow.createCell(i).setCellValue(cellValues.get(i));
        }

        return sheet;
    }
}