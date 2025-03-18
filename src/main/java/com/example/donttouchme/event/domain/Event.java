package com.example.donttouchme.event.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import com.example.donttouchme.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE event SET deleted_at = NOW() WHERE id = ?")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //고유번호 PK

    @Column
    private String thumbnailUrl; //썸네일 이미지 파일 경로

    @Column(nullable = false)
    private String eventName; //이벤트명

    @Column(nullable = false)
    private String eventType; //이벤트 유형

    @Column(nullable = false)
    private LocalDate eventDate; //이벤트 일정

    @Embedded
    private Location location; //이벤트 장소

    @Embedded
    private EventInfo eventInfo; //ON/OFF 토글 항목

    @Column
    private Integer participants; //예상 인원

    @Column
    private String amountUnit; //금액 단위

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private final List<EventDetail> eventDetails = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private final List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private final List<Target> targets = new ArrayList<>();

    public void updateEvent(String thumbnailUrl, String eventName, String eventType, LocalDate eventDate, Location location, EventInfo eventInfo, Integer participants, String amountUnit) {
        this.thumbnailUrl = thumbnailUrl;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.location = location;
        this.eventInfo = eventInfo;
        this.participants = participants;
        this.amountUnit = amountUnit;
    }

    public void setMember(Member member) {
        this.member = member;
        member.setEvents(this);
    }

    public void setEventDetail(EventDetail eventDetail) { //양방향 관계 설정
        if (!this.eventDetails.contains(eventDetail)) {
            this.eventDetails.add(eventDetail);
        }
    }


    public void setTags(Tag tag) { //양방향 관계 설정
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public void setTargets(Target target) { //양방향 관계 설정
        if (!this.targets.contains(target)) {
            this.targets.add(target);
        }
    }

    @Builder
    public Event(String eventName, String eventType, LocalDate eventDate, Location location, EventInfo eventInfo, Integer participants, Member member) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.location = location;
        this.eventInfo = eventInfo;
        this.participants = participants;
        this.member = member;
    }

    @Builder(builderMethodName = "eventBuilder", buildMethodName = "eventBuilder")
    public Event(String thumbnailUrl, String eventName, String eventType,
                 LocalDate eventDate, Location location, EventInfo eventInfo,
                 Integer participants, String amountUnit
    ) {
        this.thumbnailUrl = thumbnailUrl;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.location = location;
        this.eventInfo = eventInfo;
        this.participants = participants;
        this.amountUnit = amountUnit;
    }
}

