package com.example.donttouchme.event.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.eventdetail.domain.EventDetail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE target SET deleted_at = NOW() WHERE id = ?")
public class Target extends BaseEntity { //입금 대상 (태그 형태)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
    private final List<EventDetail> eventDetails = new ArrayList<>();

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEventDetail(EventDetail eventDetail) {
        if (!eventDetails.contains(eventDetail)) {
            eventDetails.add(eventDetail);
            eventDetail.setTarget(this);
        }
    }

    @Builder(builderMethodName = "builderOnlyValue", buildMethodName = "builderOnlyValue")
    public Target(String value) {
        this.value = value;
    }

    @Builder
    public Target(String value, Event event) {
        this.value = value;
        this.event = event;
    }
}
