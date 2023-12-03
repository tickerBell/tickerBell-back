package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.event.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveEventRequest {

    @NotBlank
    @Schema(description = "이벤트 이름", example = "eventName")
    private String name;
    @NotBlank
    @Schema(description = "이벤트 시작 시간", example = "2023-11-27T00:00:00.000Z")
    private LocalDateTime startEvent;
    @NotBlank
    @Schema(description = "이벤트 종료 시간", example = "2023-11-27T23:59:59.000Z")
    private LocalDateTime endEvent;
    @NotNull
    @Schema(description = "일단위 시작 시간", example = "20:30:00")
    private LocalTime dailyStartEvent;
    @NotBlank
    @Schema(description = "상영시간 (분단위)", example = "90")
    private Integer eventTime;
    @Schema(description = "구매 가능 시간", example = "2023-09-30T14:30:00")
    private LocalDateTime availablePurchaseTime;
    @NotBlank
    @Schema(description = "일반석 가격", example = "10000")
    private Integer normalPrice;
    @Schema(description = "특수석 가격", example = "15000")
    private Integer premiumPrice;
    @Schema(description = "자체할인", example = "1000")
    private Float saleDegree;
    @NotBlank
    @Schema(description = "배우", example = "[\"출연자1\", \"출연자2\"]")
    private List<String> castings;
    @NotBlank
    @Schema(description = "주최자", example = "[\"host1\", \"host2\"]")
    private List<String> hosts;
    @NotBlank
    @Schema(description = "이벤트 장소", example = "서울특별시")
    private String place;
    @Schema(description = "이벤트 설명", example = "공연 설명")
    private String description;
    @NotBlank
    @Schema(description = "성인여부", example = "true")
    private Boolean isAdult;
    @NotNull
    @Schema(description = "A 좌석 특별석 여부", example = "true")
    private Boolean isSpecialA;
    @NotNull
    @Schema(description = "B 좌석 특별석 여부", example = "true")
    private Boolean isSpecialB;
    @NotNull
    @Schema(description = "C 좌석 특별석 여부", example = "true")
    private Boolean isSpecialC;
    @NotNull
    @Schema(description = "카테고리", example = "MUSICAL, CONCERT, PLAY, CLASSIC, SPORTS")
    private Category category;
    @Schema(description = "태그", example = "[\"tag1\", \"tag2\"]")
    private List<String> tags = new ArrayList<>();
    @NotBlank
    @Schema(description = "썸네일 url", example = "thumbNailUrl")
    private String thumbNailUrl;
    @Schema(description = "이미지 url", example = "[\"url1\", \"url2\"]")
    private List<String> imageUrls = new ArrayList<>();

    public void setPremiumPrice(Integer premiumPrice) {
        this.premiumPrice = premiumPrice == null ? -1 :premiumPrice;
    }

    public void setSaleDegree(Float saleDegree) {
        this.saleDegree = saleDegree == null ? 0F : saleDegree;
    }

    public void setTags(List<String> tags) {
        this.tags = tags == null ? new ArrayList<>() : tags;
    }
}
