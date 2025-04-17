package com.example.donttouchme.send.service;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.send.controller.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SendQueryService {
    private final EventRepository eventRepository;
    private final JavaMailSender mailSender;

    public FindRecipientListResponse findRecipientList(final Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new IllegalArgumentException("이벤트 정보를 찾을 수 없습니다.")
        );

        List<RecipientListDto> dto = new ArrayList<>();
        for (EventDetail eventDetail : event.getEventDetails()) {
            String name = eventDetail.getName();
            String contact = eventDetail.getContact();
            if (name != null && contact != null) {
                dto.add(new RecipientListDto(eventDetail.getName(), eventDetail.getContact()));
            }
        }
        return new FindRecipientListResponse(dto);
    }

    public EmailSendResult sendAppreciationEmail(final SendEmailRequest request) {
        String contentTemplate = "{name}님, 안녕하세요! 이번 {eventName}에서 뵐 수 있어 정말 반가웠습니다.\n" +
                "귀한 시간을 내어주신 덕분에 저희에게 큰 힘이 되었습니다. 앞으로도 자주 연락드리겠습니다. 다시 한 번 감사드립니다!";
        String titleTemplate = "{name}님, {eventName}에 참여해주셔서 감사드립니다.";
        String eventName = request.eventName();
        int successCnt = 0, failCnt = 0;

        for (RecipientListDto recipient : request.recipients()) {
            String to = recipient.contact();
            String name = recipient.name();

            String replaceNameC = contentTemplate.replace("{name}", name);
            String content = replaceNameC.replace("{eventName}", eventName);

            String replaceNameTt = titleTemplate.replace("{name}", name);
            String title = replaceNameTt.replace("{eventName}", eventName);

            try {
                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(to);
                email.setSubject(title);
                email.setText(content);
                email.setFrom(request.fromEmail());

                mailSender.send(email);
                successCnt++;
            } catch (Exception e) {
                log.error("메일 전송 실패");
                failCnt++;
            }
        }

        String msg = String.format("총 %d명 중 %d명에게 메일 전송 성공, %d명 실패",
                request.recipients().size(), successCnt, failCnt);

        return new EmailSendResult(msg, successCnt, failCnt);
    }

    public String sendAppreciationSMS(final SendSMSRequest request) {

        return null;
    }
}
