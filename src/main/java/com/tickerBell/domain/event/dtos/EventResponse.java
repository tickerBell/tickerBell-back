package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.utils.SeatPriceCalculator;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private Long eventId;
    private String name;
    private LocalDateTime startEvent;
    private LocalDateTime endEvent;
    private LocalDateTime availablePurchaseTime; // 구매 가능 시간
    private LocalTime dailyStartEvent; // 하루 중 시작 시간
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

    public static EventResponse from(Event event) {

        List<String> castings = new ArrayList<>();
        for (Casting findCasting : event.getCastingList()) {
            castings.add(findCasting.getCastingName());
        }

        List<String> hosts = new ArrayList<>();
        for (Host host : event.getHostList()) {
            hosts.add(host.getHostName());
        }

        List<String> imageUrls = new ArrayList<>();
        String thumbNailUrl = null;
        for (Image image : event.getImageList()) {
            if (image.getIsThumbnail()) {
                thumbNailUrl = image.getS3Url();
            } else {
                imageUrls.add(image.getS3Url());
            }
        }

        return EventResponse.builder()
                .eventId(event.getId())
                .name(event.getName())
                .startEvent(event.getStartEvent())
                .endEvent(event.getEndEvent())
                .availablePurchaseTime(event.getAvailablePurchaseTime())
                .dailyStartEvent(event.getDailyStartEvent())
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
                .castings(castings)
                .hosts(hosts)
                .thumbNailUrl(thumbNailUrl)
                .imageUrls(imageUrls)
                .build();
    }
}
