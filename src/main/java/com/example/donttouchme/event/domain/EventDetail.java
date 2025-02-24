package com.example.donttouchme.event.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.event.domain.value.SendType;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column
    private SendType sendType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "target_id")
    private Target target;

    @OneToMany(mappedBy = "eventDetail", cascade = CascadeType.ALL)
    private final List<Tag> tags = new ArrayList<>();
}
