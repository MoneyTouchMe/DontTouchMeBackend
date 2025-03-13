package com.example.donttouchme.eventdetail.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.event.domain.SendValue;
import com.example.donttouchme.event.domain.Target;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE eventDetail SET deleted_at = NOW() WHERE id = ?")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "target_id")
    private Target target;

    @OneToOne
    @JoinColumn(name = "send_value_id")
    private SendValue sendValue;

    @OneToMany(mappedBy = "eventDetail", cascade = CascadeType.ALL)
    private final List<TagEventDetail> tagEventDetails = new ArrayList<>();

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTagEventDetails(Collection<TagEventDetail> tagEventDetails) {
        for (TagEventDetail tagEventDetail : tagEventDetails) {
            if (!this.tagEventDetails.contains(tagEventDetail)) {
                this.tagEventDetails.add(tagEventDetail);
                tagEventDetail.setEventDetail(this);
            }
        }
    }

    @Builder
    public EventDetail(String type, String history, String price, String name, String image, Event event, Target target, SendValue sendValue) {
        this.type = type;
        this.history = history;
        this.price = price;
        this.name = name;
        this.image = image;
        this.event = event;
        this.target = target;
        this.sendValue = sendValue;
    }
}
