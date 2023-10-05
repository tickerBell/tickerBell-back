package com.tickerBell.domain.ticketing.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.selectedSeat.service.SelectedSeatService;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TicketingServiceImpl implements TicketingService {
    private final TicketingRepository ticketingRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final SelectedSeatService selectedSeatService;

    // todo: 이미 공연이 끝난 ticketing 에 대해서 spring batch 로 처리

    @Override
    @Transactional
    public void saveTicketing(Long memberId, TicketingRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 예매 내역 저장
        Ticketing ticketing = Ticketing.builder()
                .event(event)
                .member(member)
                .build();
        ticketingRepository.save(ticketing);

        // 선택된 좌석 정보 저장
        SpecialSeat specialSeat = event.getSpecialSeat(); // event 프리미엄 여부 확인
        Float saleDegree = event.getSaleDegree();

        List<SelectedSeat> selectedSeatList = new ArrayList<>();
        for (String seatInfo : request.getSelectedSeat()) {
            // 이미 선택된 좌석인지 check
            // 선택 좌석 최대 개수가 2개이기 때문에 좌석 하나씩 체크
            selectedSeatService.validCheckSeatInfo(event.getId(), seatInfo);
            String[] parts = seatInfo.split("-");
            float seatPrice;
            // A좌석 선택
            if (parts[0].equals("A")) {
                if (specialSeat.getIsSpecialSeatA()) {
                    seatPrice = getSeatPrice(saleDegree, event.getPremiumPrice());
                }
                else {
                    seatPrice = getSeatPrice(saleDegree, event.getNormalPrice());
                }
            }
            // B좌석 선택
            else if (parts[0].equals("B")) {
                if (specialSeat.getIsSpecialSeatB()) {
                    seatPrice = getSeatPrice(saleDegree, event.getPremiumPrice());
                }
                else {
                    seatPrice = getSeatPrice(saleDegree, event.getNormalPrice());
                }
            }
            // C좌석 선택
            else if (parts[0].equals("C")) {
                if (specialSeat.getIsSpecialSeatC()) {
                    seatPrice = getSeatPrice(saleDegree, event.getPremiumPrice());
                }
                else {
                    seatPrice = getSeatPrice(saleDegree, event.getNormalPrice());
                }
            }
            else {
                // A, B, C 로 구분할 수 없다면 예외
                throw new CustomException(ErrorCode.SEAT_INFO_NOT_VALID_FORMAT);
            }

            SelectedSeat selectedSeat = SelectedSeat.builder().seatInfo(seatInfo).seatPrice(seatPrice).ticketing(ticketing).build();
            selectedSeatList.add(selectedSeat);
        }
        Integer savedSelectedSeatCount = selectedSeatService.saveSelectedSeat(selectedSeatList);
        log.info("저장된 선택 좌석 수: " + savedSelectedSeatCount);
    }

    @Override
    public List<TicketingResponse> getTicketingHistory(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<TicketingResponse> ticketingResponseList = ticketingRepository.findByMemberId(member.getId()).stream()
                .map(ticketing -> TicketingResponse.from(ticketing))
                .collect(Collectors.toList());
        return ticketingResponseList;
    }

    // 할인된 가격 계산
    private float getSeatPrice(float saleDegree, int price) {
        float seatPrice = 0;
        if (saleDegree == 0) { // 할인 x
            seatPrice = price;
        }
        else if (saleDegree >= 1.0) { // 상수값 할인
            seatPrice = price - saleDegree;
        }
        else if (saleDegree < 1.0 && saleDegree > 0) { // 퍼센트 할인
            seatPrice = price - (price * saleDegree);
        }
        else {
            // saleDegree 가 위 3개에 해당하지 않을 때
            throw new CustomException(ErrorCode.SALE_DEGREE_NOT_VALID_FORMAT);
        }
        return seatPrice;
    }
}
