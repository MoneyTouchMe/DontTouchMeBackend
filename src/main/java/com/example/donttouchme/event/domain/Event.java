package com.example.donttouchme.event.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.event.domain.value.EventInfo;
import com.example.donttouchme.event.domain.value.Location;
import com.example.donttouchme.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //고유번호 PK

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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private final List<EventDetail> eventDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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
}
