package com.example.donttouchme.eventdetail.repository;

import com.example.donttouchme.eventdetail.controller.dto.EventDetailListDto;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailListResponse;
import com.example.donttouchme.eventdetail.domain.QEventDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventDetailRepositoryImpl implements EventDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public EventDetailRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public FindEventDetailListResponse paginationNoOffset(
            final Long eventId,
            final Long lastEventDetailId,
            final int pageSize
    ) {
        QEventDetail eventDetail = QEventDetail.eventDetail;

        List<EventDetailListDto> eventDetails = queryFactory
                .select(Projections.constructor(EventDetailListDto.class,
                        eventDetail.id.as("eventDetailId"),
                        eventDetail.type,
                        eventDetail.history,
                        eventDetail.price,
                        eventDetail.name,
                        eventDetail.image,
                        eventDetail.contact
                ))
                .from(eventDetail)
                .where(
                        eventDetail.event.id.eq(eventId),
                        ltEventDetailId(lastEventDetailId)
                )
                .orderBy(eventDetail.id.desc())
                .limit(pageSize)
                .fetch();

        Long newLastEventDetailId = eventDetails.isEmpty() ? null : eventDetails.get(eventDetails.size() - 1).eventDetailId();

        return new FindEventDetailListResponse(eventDetails, newLastEventDetailId);
    }

    private BooleanExpression ltEventDetailId(Long lastEventDetailId) {
        if (lastEventDetailId == null) {
            return null;
        }
        return QEventDetail.eventDetail.id.lt(lastEventDetailId);
    }
}
