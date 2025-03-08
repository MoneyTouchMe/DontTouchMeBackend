package com.example.donttouchme.event.repository;

import com.example.donttouchme.event.controller.dto.FindEventListResponse;
import com.example.donttouchme.event.domain.QEvent;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public EventRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<FindEventListResponse> paginationNoOffset(Long memberId, Long lastEventId, int pageSize) {
        QEvent event = QEvent.event;

        return queryFactory
                .select(Projections.fields(FindEventListResponse.class,
                        event.id.as("eventId"),
                        event.eventName,
                        event.eventDate,
                        event.thumbnailUrl,
                        event.eventType,
                        event.location.address
                ))
                .from(event)
                .where(
                        event.member.id.eq(memberId), //memberId로 필터링
                        ltEventId(lastEventId) //No Offset 방식 : lastEventId보다 작은 데이터만 조회
                )
                .orderBy(event.id.desc()) //내림차순
                .limit(pageSize) //페이지 사이즈만큼 제한
                .fetch();
    }

    private BooleanExpression ltEventId(Long lastEventId) {
        if (lastEventId == null) {
            return null; // 첫 페이지일 경우 조건이 없어서 null을 반환
        }
        return QEvent.event.id.lt(lastEventId);  // lastEventId보다 작은 이벤트만 반환
    }
}
