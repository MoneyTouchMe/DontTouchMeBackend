package com.example.donttouchme.eventdetail.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.TagEventDetail;
import com.example.donttouchme.event.domain.Target;
import com.example.donttouchme.eventdetail.controller.dto.UpdateEventDetailRequest;
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
@SQLDelete(sql = "UPDATE event_detail SET deleted_at = NOW() WHERE id = ?")
public class EventDetail extends BaseEntity { //입출금 내역

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //고유번호

    @Column
    private String type; //입출금 분류 ex(입금, 출금)

    @Column
    private String history; //입출금 내역명

    @Column
    private String price; //금액

    @Column
    private String name; //입출금자명

    @Column
    private String image; //첨부한 입출금 이미지 파일 경로

    @Column(nullable = false)
    private String contact; //연락처

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Target target;

    @OneToMany(mappedBy = "eventDetail", cascade = CascadeType.ALL)
    private final List<TagEventDetail> tagEventDetails = new ArrayList<>();

    public void update(UpdateEventDetailRequest request, Target target) {
        this.type = request.type();
        this.history = request.history();
        this.price = request.price();
        this.name = request.name();
        this.image = request.imageUrl();
        this.contact = request.contact();
        setTarget(target);
    }

    public void setEvent(Event event) {
        this.event = event;
        event.setEventDetail(this);
    }

    public void setTarget(Target target) {
        this.target = target;
        if (target != null) {
            target.setEventDetail(this);
        }
    }


    public void setTagEventDetail(TagEventDetail tagEventDetail) {
        if (!this.tagEventDetails.contains(tagEventDetail)) {
            this.tagEventDetails.add(tagEventDetail);
        }
    }

    @Builder
    public EventDetail(String type, String history, String price, String name, String image, String contact, Event event, Target target) {
        this.type = type;
        this.history = history;
        this.price = price;
        this.name = name;
        this.image = image;
        this.contact = contact;
        setEvent(event);
        setTarget(target);
    }

    @Builder(builderMethodName = "builderOnlyField", buildMethodName = "builderOnlyField")
    public EventDetail(String type, String history, String price, String name, String image, String contact) {
        this.type = type;
        this.history = history;
        this.price = price;
        this.name = name;
        this.image = image;
        this.contact = contact;
    }
}
