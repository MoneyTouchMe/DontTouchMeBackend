package com.example.donttouchme.send.service;

import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.repository.EventRepository;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.send.controller.dto.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
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
    private DefaultMessageService messageService;
    @Value("${solapi.apiKey}")
    private String apiKey;
    @Value("${solapi.apiSecret}")
    private String apiSecret;
    @Value("${solapi.sender}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                "https://api.solapi.com"
        );
    }

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

            String content = contentTemplate.replace("{name}", name).replace("{eventName}", eventName);
            String title = titleTemplate.replace("{name}", name).replace("{eventName}", eventName);

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

    public SMSSendResult sendAppreciationSMS(final SendSMSRequest request) {
        int successCnt = 0, failCnt = 0;
        String contentTemplate = "{name}님, 안녕하세요! 이번 {eventName}에서 뵐 수 있어 정말 반가웠습니다.\n" +
                "귀한 시간을 내어주신 덕분에 저희에게 큰 힘이 되었습니다. 앞으로도 자주 연락드리겠습니다. 다시 한 번 감사드립니다!";

        for (RecipientListDto dto : request.recipients()) {
            String to = dto.contact().replaceAll("-", "");
            String text = contentTemplate.replace("{name}", dto.name()).replace("{eventName}", request.eventName());

            Message message = new Message();
            message.setFrom(fromNumber); // Solapi에 등록된 발신번호
            message.setTo(to);
            message.setText(text);
            try {
                messageService.send(message);
                successCnt++;
            } catch (Exception e) {
                log.error("SMS 전송 중 예외 발생: {}", e.getMessage());
                failCnt++;
            }
        }
        String msg = String.format("총 %d명 중 %d명 성공, %d명 실패", request.recipients().size(), successCnt, failCnt);

        return new SMSSendResult(msg, successCnt, failCnt);
    }
}

