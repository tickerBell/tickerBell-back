package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.utils.SeatPriceCalculator;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private String eventId;
    private String name;
    private LocalDateTime startEvent;
    private LocalDateTime endEvent;
    private Integer normalPrice;
    private Integer premiumPrice;
    private Float discountNormalPrice;
    private Float discountPremiumPrice;
    private List<String> hosts;
    private List<String> castings;
    private String place;
    private Boolean isAdult;
    private Category category;
    private Boolean isSpecialSeatA;
    private Boolean isSpecialSeatB;
    private Boolean isSpecialSeatC;
    private String thumbNailUrl;
    private List<String> imageUrls;

    // todo 주최자 별도 처리
    public static EventResponse from(Event event) {

        return EventResponse.builder()
                .name(event.getName())
                .startEvent(event.getStartEvent())
                .endEvent(event.getEndEvent())
                .normalPrice(event.getNormalPrice())
                .premiumPrice(event.getPremiumPrice())
                .discountNormalPrice(SeatPriceCalculator.getSeatPrice(event.getSaleDegree(), event.getNormalPrice()))
                .discountPremiumPrice(SeatPriceCalculator.getSeatPrice(event.getSaleDegree(), event.getPremiumPrice()))
                .place(event.getPlace())
                .isAdult(event.getIsAdult())
                .category(event.getCategory())
                .isSpecialSeatA(event.getSpecialSeat().getIsSpecialSeatA())
                .isSpecialSeatB(event.getSpecialSeat().getIsSpecialSeatB())
                .isSpecialSeatC(event.getSpecialSeat().getIsSpecialSeatC())
                .build();
    }
}
