package com.example.donttouchme.eventdetail.repository;

import com.example.donttouchme.event.domain.QTag;
import com.example.donttouchme.event.domain.QTagEventDetail;
import com.example.donttouchme.event.domain.QTarget;
import com.example.donttouchme.eventdetail.controller.dto.EventDetailListDto;
import com.example.donttouchme.eventdetail.controller.dto.FindEventDetailListResponse;
import com.example.donttouchme.eventdetail.domain.QEventDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EventDetailRepositoryImpl implements EventDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final static QEventDetail eventDetail = QEventDetail.eventDetail;
    private final static QTarget target = QTarget.target;
    private final static QTagEventDetail tagEventDetail = QTagEventDetail.tagEventDetail;
    private final static QTag tag = QTag.tag;

    public EventDetailRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public FindEventDetailListResponse paginationNoOffset(
            final Long eventId,
            final Long lastEventDetailId,
            final int pageSize
    ) {
        // 메인 쿼리 실행
        List<EventDetailWithTarget> mainResults = fetchEventDetailsWithTarget(eventId, lastEventDetailId, pageSize);

        if (mainResults.isEmpty()) {
            return new FindEventDetailListResponse(Collections.emptyList(), null);
        }

        // EventDetail ID 목록 추출
        List<Long> eventDetailIds = extractEventDetailIds(mainResults);

        // Tag 정보 조회
        Map<Long, List<String>> tagValuesByDetailId = fetchTagValuesByDetailId(eventDetailIds);

        // 최종 DTO 생성
        List<EventDetailListDto> eventDetails = createEventDetailListDtos(mainResults, tagValuesByDetailId);

        // 응답 생성
        return createFindEventDetailListResponse(eventDetails);
    }

    private List<EventDetailWithTarget> fetchEventDetailsWithTarget(Long eventId, Long lastEventDetailId, int pageSize) {
        return queryFactory
                .select(Projections.constructor(EventDetailWithTarget.class,
                        eventDetail.id.as("eventDetailId"),
                        eventDetail.type,
                        eventDetail.history,
                        eventDetail.price,
                        eventDetail.name,
                        eventDetail.image,
                        eventDetail.contact,
                        target.value.as("targetValue")
                ))
                .from(eventDetail)
                .leftJoin(eventDetail.target, target)
                .where(
                        eventDetail.event.id.eq(eventId),
                        ltEventDetailId(lastEventDetailId)
                )
                .orderBy(eventDetail.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private List<Long> extractEventDetailIds(List<EventDetailWithTarget> mainResults) {
        return mainResults.stream()
                .map(EventDetailWithTarget::eventDetailId)
                .toList();
    }

    private Map<Long, List<String>> fetchTagValuesByDetailId(List<Long> eventDetailIds) {
        return queryFactory
                .select(tagEventDetail.eventDetail.id, tag.value)
                .from(tagEventDetail)
                .join(tagEventDetail.tag, tag)
                .where(tagEventDetail.eventDetail.id.in(eventDetailIds))
                .fetch()
                .stream()
                .filter(tuple -> tuple.get(0, Long.class) != null)
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(0, Long.class),
                        Collectors.mapping(
                                tuple -> Optional.ofNullable(tuple.get(1, String.class)).orElse(""),
                                Collectors.toList()
                        )
                ));
    }

    private List<EventDetailListDto> createEventDetailListDtos(
            List<EventDetailWithTarget> mainResults,
            Map<Long, List<String>> tagValuesByDetailId
    ) {
        return mainResults.stream()
                .map(result -> new EventDetailListDto(
                        result.eventDetailId(),
                        result.type(),
                        result.history(),
                        result.price(),
                        result.name(),
                        result.image(),
                        result.contact(),
                        result.targetValue(),
                        tagValuesByDetailId.getOrDefault(result.eventDetailId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    private FindEventDetailListResponse createFindEventDetailListResponse(List<EventDetailListDto> eventDetails) {
        Long newLastEventDetailId = eventDetails.isEmpty() ? null : eventDetails.get(eventDetails.size() - 1).eventDetailId();
        return new FindEventDetailListResponse(eventDetails, newLastEventDetailId);
    }

    private BooleanExpression ltEventDetailId(Long lastEventDetailId) {
        if (lastEventDetailId == null) {
            return null;
        }
        return QEventDetail.eventDetail.id.lt(lastEventDetailId);
    }

    public record EventDetailWithTarget(
            Long eventDetailId,
            String type,
            String history,
            String price,
            String name,
            String image,
            String contact,
            String targetValue
    ) {
    }
}

