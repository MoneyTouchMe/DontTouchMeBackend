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

import java.util.Objects;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tag_event_detail SET deleted_at = NOW() WHERE id = ?")
public class TagEventDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_detail_id")
    private EventDetail eventDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public void setTag(Tag tag) {
        this.tag = tag;
        tag.setTagEventDetail(this);
    }

    public void setEventDetail(EventDetail eventDetail) {
        this.eventDetail = eventDetail;
        eventDetail.setTagEventDetail(this);
    }

    @Builder
    public TagEventDetail(EventDetail eventDetail, Tag tag) {
        setEventDetail(eventDetail);
        setTag(tag);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TagEventDetail that = (TagEventDetail) object;
        return Objects.equals(getEventDetail(), that.getEventDetail()) && Objects.equals(getTag(), that.getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventDetail(), getTag());
    }
}
