package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.tag.entity.Tag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
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
    private List<String> castings;
    private String place;
    private Boolean isAdult;
    private Category category;
    private Boolean isSpecialSeatA;
    private Boolean isSpecialSeatB;
    private Boolean isSpecialSeatC;
    private String thumbNailUrl;
    private List<String> imageUrls;
    private List<String> tags;

    // todo 주최자 별도 처리
    public static EventResponse from(Event event) {

        List<Host> eventHosts = event.getHosts();
        List<String> hosts = new ArrayList<>();
        for (Host eventHost : eventHosts) {
            hosts.add(eventHost.getHostName());
        }

        List<Casting> eventCastings = event.getCastings();
        List<String> castings = new ArrayList<>();
        for (Casting eventCasting : eventCastings) {
            castings.add(eventCasting.getCastingName());
        }

        List<Tag> eventTags = event.getTags();
        List<String> tags = new ArrayList<>();
        for (Tag eventTag : eventTags) {
            tags.add(eventTag.getTagName());
        }

        EventResponseBuilder builder = EventResponse.builder()
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
                .hosts(hosts)
                .castings(castings)
                .tags(tags);

        List<Image> eventImages = event.getImages();
        List<String> images = new ArrayList<>();
        for (Image eventImage : eventImages) {
            if (eventImage.getIsThumbnail()) {
                builder.thumbNailUrl(eventImage.getS3Url());
            } else {
                images.add(eventImage.getS3Url());
            }
        }

        return builder.imageUrls(images)
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
