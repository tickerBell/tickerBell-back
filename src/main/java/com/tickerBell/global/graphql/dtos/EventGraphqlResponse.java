package com.tickerBell.global.graphql.dtos;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.utils.SeatPriceCalculator;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventGraphqlResponse {
    private Long eventId;
    private String name;
    private LocalDateTime startEvent;
    private LocalDateTime endEvent;
    private LocalDateTime availablePurchaseTime; // 구매 가능 시간
    private Integer eventTime; // 이벤트 시간
    private Integer normalPrice;
    private Integer premiumPrice;
    private Float saleDegree; // 1.0 이상: n 원 할인  |  1.0 미만: n 퍼센트 할인 | 0: 세일 x

    private Float discountNormalPrice;
    private Float discountPremiumPrice;

    private List<String> hosts; // 호스트
    private List<String> castings; // 캐스팅

    private Integer totalSeat; // 전체 좌석 수
    private Integer remainSeat; // 남은 좌석 수
    private String place;
    private Boolean isAdult;
    private Integer viewCount; // 조회수
    private Category category;
    private Boolean isCancelled; // 취소 여부

    private Boolean isSpecialSeatA;
    private Boolean isSpecialSeatB;
    private Boolean isSpecialSeatC;

    private String thumbNailUrl;
    private List<String> imageUrls;

    public static EventGraphqlResponse from(Event event) {
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


        return EventGraphqlResponse.builder()
                .eventId(event.getId())
                .name(event.getName())
                .startEvent(event.getStartEvent())
                .endEvent(event.getEndEvent())
                .availablePurchaseTime(event.getAvailablePurchaseTime())
                .eventTime(event.getEventTime())
                .normalPrice(event.getNormalPrice())
                .premiumPrice(event.getPremiumPrice())
                .saleDegree(event.getSaleDegree())

                .discountNormalPrice(SeatPriceCalculator.getSeatPrice(event.getSaleDegree(), event.getNormalPrice()))
                .discountPremiumPrice(SeatPriceCalculator.getSeatPrice(event.getSaleDegree(), event.getPremiumPrice()))

                .castings(castings)
                .hosts(hosts)

                .totalSeat(event.getTotalSeat())
                .remainSeat(event.getRemainSeat())
                .place(event.getPlace())
                .isAdult(event.getIsAdult())
                .viewCount(event.getViewCount())
                .category(event.getCategory())
                .isCancelled(event.getIsCancelled())

                .isSpecialSeatA(event.getSpecialSeat().getIsSpecialSeatA())
                .isSpecialSeatB(event.getSpecialSeat().getIsSpecialSeatB())
                .isSpecialSeatC(event.getSpecialSeat().getIsSpecialSeatC())

                .thumbNailUrl(thumbNailUrl)
                .imageUrls(imageUrls)
                .build();
    }
}
