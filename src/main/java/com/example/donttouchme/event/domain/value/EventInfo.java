package com.example.donttouchme.event.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventInfo { //이벤트 ON/OFF 토글 항목

    @Column(nullable = false)
    private boolean isType; //입출금 분류 여부

    @Column(nullable = false)
    private boolean isHistory; //입출금 내역명 표시 여부

    @Column(nullable = false)
    private boolean isPrice; //금액 태그 사용 여부

    @Column(nullable = false)
    private boolean isName; //입출금 대상 이름 표시 여부

    @Column(nullable = false)
    private boolean isTag; //카테고리 설정 여부

    @Column(nullable = false)
    private boolean isImage; //사진 첨부 여부

    @Column(nullable = false)
    private boolean isSide; //입금 대상 지정 여부

    @Column(nullable = false)
    private boolean isSend; //감사장 전송 기능 여부

    @Builder
    public EventInfo(
            final boolean isType,
            final boolean isHistory,
            final boolean isPrice,
            final boolean isName,
            final boolean isTag,
            final boolean isImage,
            final boolean isSide,
            final boolean isSend
    ) {
        this.isType = isType;
        this.isHistory = isHistory;
        this.isPrice = isPrice;
        this.isName = isName;
        this.isTag = isTag;
        this.isImage = isImage;
        this.isSide = isSide;
        this.isSend = isSend;
    }

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
