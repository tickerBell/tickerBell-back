package com.tickerBell.domain.event.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCategoryResponse {
    private List<EventListResponse> eventListResponses = new ArrayList<>();
    private Long totalCount;
}
