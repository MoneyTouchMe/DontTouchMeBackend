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
import java.util.Objects;

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
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<EventDetail> eventDetails = new ArrayList<>();

    public void setEvent(Event event) {
        this.event = event;
        event.setTargets(this);
    }

    public void setEventDetail(EventDetail eventDetail) {
        if (!eventDetails.contains(eventDetail)) {
            eventDetails.add(eventDetail);
        }
    }

    @Builder(builderMethodName = "builderOnlyValue", buildMethodName = "builderOnlyValue")
    public Target(String value) {
        this.value = value;
    }

    @Builder
    public Target(String value, Event event) {
        this.value = value;
        setEvent(event);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Target target = (Target) object;
        return Objects.equals(getValue(), target.getValue()) && Objects.equals(getEvent(), target.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getEvent());
    }
}
