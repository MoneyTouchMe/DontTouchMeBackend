package com.example.donttouchme.event.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventInfo {

    @Column(nullable = false)
    private boolean isType;

    @Column(nullable = false)
    private boolean isHistory;

    @Column(nullable = false)
    private boolean isPrice;

    @Column(nullable = false)
    private boolean isName;

    @Column(nullable = false)
    private boolean isTag;

    @Column(nullable = false)
    private boolean isImage;

    @Column(nullable = false)
    private boolean isSide;

    @Column(nullable = false)
    private boolean isSend;

}
