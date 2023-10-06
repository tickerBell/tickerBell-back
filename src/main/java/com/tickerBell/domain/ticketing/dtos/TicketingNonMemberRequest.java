package com.tickerBell.domain.ticketing.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketingNonMemberRequest {
    @Schema(description = "선택한 좌석 리스트. 최대 2개", example = "[\"A-1\", \"B-2\"]")
    @Size(max = 2, message = "좌석은 최대 2개까지 선택 가능")
    private List<String> selectedSeat; // 선택 좌석
    @Schema(description = "이벤트 ID", example = "1")
    private Long eventId; // 이벤트
    @Schema(description = "비회원 예매 시 입력 이름", example = "사용자1")
    private String name; // 비회원 예매 시 입력 이름
    @Schema(description = "비회원 예매 시 입력 휴대폰 번호", example = "01012345678")
    private String phone; // 비회원 예매 시 입력 휴대폰 번호
}
