package com.example.donttouchme.excel.service;

import com.example.donttouchme.event.domain.Event;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExcelQueryService {

    private final EventRepository eventRepository;


    public byte[] exportSampleExcel(
            final Long eventId
    ) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("해당 하는 event가 없습니다.")
        );

        try (
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ){
            Sheet sheet = workbook.createSheet(event.getEventName());

            List<String> cellValues = event.getEventInfo().toCellValues();

            Row headerRow = sheet.createRow(0);

            for(int i = 0; i < cellValues.size(); i++) {
                headerRow.createCell(i).setCellValue(cellValues.get(i));
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("엑셀양식 추출 실패");
        }
    }
}
