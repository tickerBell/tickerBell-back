package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.event.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveEventRequest {

    @Schema(description = "이벤트 이름", example = "eventName")
    private String name;
    @Schema(description = "이벤트 시작 시간", example = "2023-09-29T14:30:00")
    private LocalDateTime startEvent;
    @Schema(description = "이벤트 시작 시간", example = "2023-09-30T14:30:00")
    private LocalDateTime endEvent;
    @Schema(description = "일반석 가격", example = "10000")
    private Integer normalPrice;
    @Schema(description = "특수석 가격", example = "15000")
    private Integer premiumPrice;
    @Schema(description = "자체할인", example = "1000")
    private Float saleDegree;
    @Schema(description = "배우", example = "출연자1, 출연자2")
    private String casting;
    @Schema(description = "주최자", example = "host")
    private String host;
    @Schema(description = "이벤트 장소", example = "서울특별시")
    private String place;
    @Schema(description = "성인여부", example = "true")
    private Boolean isAdult;
    @Schema(description = "A 좌석 특별석 여부", example = "true")
    private Boolean isSpecialA;
    @Schema(description = "B 좌석 특별석 여부", example = "true")
    private Boolean isSpecialB;
    @Schema(description = "C 좌석 특별석 여부", example = "true")
    private Boolean isSpecialC;
    @Schema(description = "카테고리", example = "[MUSICAL, CONCERT, PLAY, CLASSIC, SPORTS] 중 하나만 STRING 으로 요청")
    private Category category;
    @Schema(description = "태그", example = "[\"tag1\", \"tag2\"]")
    private List<String> tags;
}
