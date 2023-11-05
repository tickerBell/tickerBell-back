package com.tickerBell.domain.ticketing.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.NonMember;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.member.repository.NonMemberRepository;
import com.tickerBell.domain.selectedSeat.repository.SelectedSeatRepository;
import com.tickerBell.domain.selectedSeat.service.SelectedSeatService;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberCancelRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberRequest;
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
import java.util.Optional;
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
    private final NonMemberRepository nonMemberRepository;
    private final SelectedSeatRepository selectedSeatRepository;

    @Override
    @Transactional
    public Long saveTicketing(Long memberId, TicketingRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));

        // 예매 내역 저장
        Ticketing ticketing = Ticketing.builder()
                .event(event)
                .member(member)
                .build();
        Ticketing savedTicketing = ticketingRepository.save(ticketing);

        // 선택된 좌석 정보 저장
        SpecialSeat specialSeat = event.getSpecialSeat(); // event 프리미엄 여부 확인
        Float saleDegree = event.getSaleDegree();

        List<SelectedSeat> selectedSeatList = createSelectedSeatList(
                request.getSelectedSeat(), event, ticketing, specialSeat, saleDegree);
        Integer savedSelectedSeatCount = selectedSeatService.saveSelectedSeat(selectedSeatList);
        log.info("저장된 선택 좌석 수: " + savedSelectedSeatCount);

        // event 남은 좌석 수 업데이트
        event.setRemainSeat(event.getRemainSeat() - savedSelectedSeatCount);
        return savedTicketing.getId();
    }



    @Override
    @Transactional
    public Long saveTicketingNonMember(TicketingNonMemberRequest request) {
        Optional<NonMember> findNonMember = nonMemberRepository.findByNameAndPhone(request.getName(), request.getPhone());
        NonMember nonMember;
        // 이름, 핸드폰 조합의 정보가 DB 에 있을 때
        if (findNonMember.isPresent()) {
            nonMember = findNonMember.get();
        }
        // DB 에 없을 땐 새로 저장
        else {
            NonMember nonMemberEntity = NonMember.builder()
                    .name(request.getName())
                    .phone(request.getPhone())
                    .build();
            NonMember savedNonMember = nonMemberRepository.save(nonMemberEntity);
            nonMember = savedNonMember;
        }

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new CustomException(ErrorCode.EVENT_NOT_FOUND));

        // 예매 내역 저장
        Ticketing ticketing = Ticketing.builder()
                .event(event)
                .nonMember(nonMember)
                .build();
        Ticketing savedTicketing = ticketingRepository.save(ticketing);

        SpecialSeat specialSeat = event.getSpecialSeat();

        // 비회원일 경우 sale 없음
        List<SelectedSeat> selectedSeatList = createSelectedSeatList(
                request.getSelectedSeat(), event, ticketing, specialSeat, 0F);

        Integer savedSelectedSeatCount = selectedSeatService.saveSelectedSeat(selectedSeatList);
        log.info("저장된 선택 좌석 수: " + savedSelectedSeatCount);

        // event 남은 좌석 수 업데이트
        event.setRemainSeat(event.getRemainSeat() - savedSelectedSeatCount);
        return savedTicketing.getId();
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

    @Override
    public List<TicketingResponse> getTicketingHistoryNonMember(String name, String phone) {
        NonMember nonMember = nonMemberRepository.findByNameAndPhone(name, phone)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_MEMBER_NOT_FOUND));

        List<TicketingResponse> ticketingResponseList = ticketingRepository.findByNonMemberId(nonMember.getId()).stream()
                .map(ticketing -> TicketingResponse.from(ticketing))
                .collect(Collectors.toList());
        return ticketingResponseList;
    }

    @Override
    @Transactional
    public void cancelTicketing(Long memberId, Long ticketingId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Ticketing ticketing = ticketingRepository.findByIdWithEvent(ticketingId)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKETING_NOT_FOUND));

        // remain seat 수 업데이트
        Event event = ticketing.getEvent();
        event.setRemainSeat(event.getRemainSeat() + ticketing.getSelectedSeatList().size());

        ticketingRepository.delete(ticketing);
    }

    @Override
    @Transactional
    public void cancelTicketingNonMember(TicketingNonMemberCancelRequest request, Long ticketingId) {
        Ticketing ticketing = ticketingRepository.findByIdAndNonMemberWithEvent(ticketingId, request.getName(), request.getPhone())
                .orElseThrow(() -> new CustomException(ErrorCode.TICKETING_NOT_FOUND));

        // remain seat 수 업데이트
        Event event = ticketing.getEvent();
        event.setRemainSeat(event.getRemainSeat() + ticketing.getSelectedSeatList().size());
        
        ticketingRepository.delete(ticketing);
    }

    private List<SelectedSeat> createSelectedSeatList(List<String> selectedSeatRequest, Event event, Ticketing ticketing, SpecialSeat specialSeat, Float saleDegree) {
        List<SelectedSeat> selectedSeatList = new ArrayList<>();

        for (String seatInfo : selectedSeatRequest) {
            // 이미 선택된 좌석인지 check
            // 선택 좌석 최대 개수가 2개이기 때문에 좌석 하나씩 체크
            selectedSeatService.validCheckSeatInfo(event.getId(), seatInfo);
            String[] parts = seatInfo.split("-");
            float seatPrice;
            // A좌석 선택
            if (parts[0].equals("A")) {
                if (specialSeat.getIsSpecialSeatA()) {
                    seatPrice = getSeatPrice(saleDegree, event.getPremiumPrice());
                } else {
                    seatPrice = getSeatPrice(saleDegree, event.getNormalPrice());
                }
            }
            // B좌석 선택
            else if (parts[0].equals("B")) {
                if (specialSeat.getIsSpecialSeatB()) {
                    seatPrice = getSeatPrice(saleDegree, event.getPremiumPrice());
                } else {
                    seatPrice = getSeatPrice(saleDegree, event.getNormalPrice());
                }
            }
            // C좌석 선택
            else if (parts[0].equals("C")) {
                if (specialSeat.getIsSpecialSeatC()) {
                    seatPrice = getSeatPrice(saleDegree, event.getPremiumPrice());
                } else {
                    seatPrice = getSeatPrice(saleDegree, event.getNormalPrice());
                }
            } else {
                // A, B, C 로 구분할 수 없다면 예외
                throw new CustomException(ErrorCode.SEAT_INFO_NOT_VALID_FORMAT);
            }

            SelectedSeat selectedSeat = SelectedSeat.builder().seatInfo(seatInfo).seatPrice(seatPrice).ticketing(ticketing).build();
            selectedSeatList.add(selectedSeat);
        }
        return selectedSeatList;
    }

    // 할인된 가격 계산
    private float getSeatPrice(float saleDegree, int price) {
        float seatPrice = 0;
        if (saleDegree == 0) { // 할인 x
            seatPrice = price;
        } else if (saleDegree >= 1.0) { // 상수값 할인
            seatPrice = price - saleDegree;
        } else if (saleDegree < 1.0 && saleDegree > 0) { // 퍼센트 할인
            seatPrice = price - (price * saleDegree);
        } else {
            // saleDegree 가 위 3개에 해당하지 않을 때
            throw new CustomException(ErrorCode.SALE_DEGREE_NOT_VALID_FORMAT);
        }
        return seatPrice;
    }
}
