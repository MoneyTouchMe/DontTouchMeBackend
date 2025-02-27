package com.example.donttouchme.event.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
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
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
public class EventDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;

    @Column
    private String history;

    @Column
    private String price;

    @Column
    private String name;

    @Column
    private String image;

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
    private final List<TagEventDetail> tags = new ArrayList<>();


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
