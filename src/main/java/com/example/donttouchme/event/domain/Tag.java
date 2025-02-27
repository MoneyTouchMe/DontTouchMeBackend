package com.example.donttouchme.event.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
public class Tag extends BaseEntity { //태그

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value; //태그 값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_detail_id")
    private EventDetail eventDetail;

    @Builder
    public Tag(String value, EventDetail eventDetail) {
        this.value = value;
        this.eventDetail = eventDetail;
    }
}
