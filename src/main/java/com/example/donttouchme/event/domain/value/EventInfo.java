package com.example.donttouchme.event.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> toCellValues() {
        List<String> cellValues = new ArrayList<>();
        if (isType) cellValues.add("입출금 분류");
        if (isHistory) cellValues.add("입출금 내역명");
        if (isPrice) cellValues.add("금액");
        if (isName) cellValues.add("이름");
        if (isTag) cellValues.add("태그");
        if (isSide) cellValues.add("입금대상");
        return cellValues;
    }

}
