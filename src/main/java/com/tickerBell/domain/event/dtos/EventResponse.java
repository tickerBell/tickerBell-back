package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private String name;
    private LocalDateTime startEvent;
    private LocalDateTime endEvent;
    private Integer normalPrice;
    private Integer premiumPrice;
    private Float discountNormalPrice;
    private Float discountPremiumPrice;
    private List<String> hosts;
    private String place;
    private Boolean isAdult;
    private Category category;
    private Boolean isSpecialSeatA;
    private Boolean isSpecialSeatB;
    private Boolean isSpecialSeatC;

    // todo 주최자 별도 처리
    public static EventResponse from(Event event) {
        return EventResponse.builder()
                .name(event.getName())
                .startEvent(event.getStartEvent())
                .endEvent(event.getEndEvent())
                .normalPrice(event.getNormalPrice())
                .premiumPrice(event.getPremiumPrice())
                .discountNormalPrice(discount(event.getNormalPrice(), event.getSaleDegree()))
                .discountPremiumPrice(discount(event.getPremiumPrice(), event.getSaleDegree()))
                .place(event.getPlace())
                .isAdult(event.getIsAdult())
                .category(event.getCategory())
                .isSpecialSeatA(event.getSpecialSeat().getIsSpecialSeatA())
                .isSpecialSeatB(event.getSpecialSeat().getIsSpecialSeatB())
                .isSpecialSeatC(event.getSpecialSeat().getIsSpecialSeatC())
                .build();
    }

    private static Float discount(Integer price, Float saleDegree) {
        if (saleDegree != null) {
            if (saleDegree < 1 && saleDegree > 0) {
                return price - ((Float) (price * saleDegree));
            } else if (saleDegree >= 1) {
                return price - saleDegree;
            } else {
                return price.floatValue();
            }
        } else {
            return price.floatValue();
        }
    }
}
