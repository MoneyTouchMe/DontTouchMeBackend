package com.example.donttouchme.tag.domain;

import com.example.donttouchme.common.Entity.BaseEntity;
import com.example.donttouchme.event.domain.Event;
import com.example.donttouchme.eventdetail.domain.TagEventDetail;
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
import java.util.Objects;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tag SET deleted_at = NOW() WHERE id = ?")
public class Tag extends BaseEntity { //태그

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value; //태그 값


    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private final List<TagEventDetail> tagEventDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTagEventDetails(Collection<TagEventDetail> tagEventDetails) {
        for (TagEventDetail tagEventDetail : tagEventDetails) {
            if (!this.tagEventDetails.contains(tagEventDetail)) {
                this.tagEventDetails.add(tagEventDetail);
                tagEventDetail.setTag(this);
            }
        }
    }

    @Builder
    public Tag(String value, Event event) {
        this.value = value;
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(value, tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
