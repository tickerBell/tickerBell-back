package com.tickerBell.domain.event.service;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.casting.repository.CastingRepository;
import com.tickerBell.domain.event.dtos.*;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.host.repository.HostRepository;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import com.tickerBell.domain.image.service.ImageService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.specialseat.service.SpecialSeatService;
import com.tickerBell.domain.tag.entity.Tag;
import com.tickerBell.domain.tag.service.TagService;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final Integer TOTALSEAT = 60; // 총 좌석 수 60으로 고정
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final SpecialSeatService specialSeatService;
    private final TagService tagService;
    private final HostRepository hostRepository;
    private final CastingRepository castingRepository;
    private final ImageService imageService;
    private final TicketingRepository ticketingRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public Long saveEvent(Long memberId, SaveEventRequest request) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 특수석 정보 저장
        SpecialSeat specialSeat = specialSeatService.saveSpecialSeat(request.getIsSpecialA(), request.getIsSpecialB(), request.getIsSpecialC());

        LocalDateTime availablePurchaseTime = checkAvailablePurchaseTime(request.getAvailablePurchaseTime(), request.getStartEvent());

        // event 저장
        Event event = Event.builder()
                .name(request.getName())
                .startEvent(request.getStartEvent())
                .endEvent(request.getEndEvent())
                .availablePurchaseTime(availablePurchaseTime)
                .normalPrice(request.getNormalPrice())
                .premiumPrice(request.getPremiumPrice())
                .saleDegree(request.getSaleDegree())
                .totalSeat(TOTALSEAT)
                .remainSeat(TOTALSEAT) // remainSeat 는 등록 시 totalSeat 와 같다고 구현
                .isAdult(request.getIsAdult())
                .place(request.getPlace())
                .category(request.getCategory())
                .member(findMember) // member 연관관계
                .specialSeat(specialSeat) // special seat 연관관계
                .build();

        // tag 저장
        List<String> tagNameList = request.getTags();
        List<Tag> tagList = tagNameList.stream()
                .map(tagName -> new Tag(tagName, event, findMember))
                .collect(Collectors.toList());

        Integer savedTagSize = tagService.saveTagList(tagList);
        log.info("저장된 태그 수: " + savedTagSize);

        // 주최자 저장
        List<String> hosts = request.getHosts();
        for (String host : hosts) {
            hostRepository.save(Host.builder().hostName(host).event(event).build());
        }

        // 출연자 저장
        List<String> castings = request.getCastings();
        for (String casting : castings) {
            castingRepository.save(Casting.builder().castingName(casting).event(event).build());
        }

        Event savedEvent = eventRepository.save(event);
        Long savedEventId = savedEvent.getId();

        List<String> imageUrls = request.getImageUrls();
        for (String imageUrl : imageUrls) {
            imageService.updateEventByImageUrl(imageUrl, savedEventId);
        }

        return savedEventId;
    }

    private LocalDateTime checkAvailablePurchaseTime(LocalDateTime availablePurchaseTime, LocalDateTime startTime) {
        if (availablePurchaseTime == null) {
            return startTime.minus(Period.ofWeeks(2)); // 구매 가능 시간이 없으면 시작 시간에서 2주 전 시간 반환
        } else {
            return availablePurchaseTime; // 구매 가능 시간이 존재하면 바로 리턴
        }
    }

    @Override
    public EventCategoryResponse getEventByCategory(Category category, Pageable pageable) {
        Page<Event> findEventsPage = eventRepository.findByCategoryFetchAllPage(category, pageable);

        List<Event> findEvents = findEventsPage.getContent();

        List<EventListResponse> responses = new ArrayList<>();
        for (Event event : findEvents) {
            EventListResponse response = EventListResponse.from(event);
            Image thumbNailImage = imageRepository.findThumbNailImageByEventId(event.getId());
            response.setThumbNailImage(thumbNailImage.getS3Url());
            responses.add(response);
        }

        EventCategoryResponse eventCategoryResponse = new EventCategoryResponse();
        eventCategoryResponse.setEventListResponses(responses);
        eventCategoryResponse.setTotalCount(findEventsPage.getTotalElements());

        return eventCategoryResponse;
    }

    @Override
    @Transactional
    public EventResponse findByIdFetchAll(Long eventId) {
        Event findEvent = eventRepository.findByIdFetchAll(eventId);
        EventResponse response = EventResponse.from(findEvent);

        List<Host> findHosts = hostRepository.findByEventId(findEvent.getId());
        List<String> hosts = new ArrayList<>();
        for (Host findHost : findHosts) {
            hosts.add(findHost.getHostName());
        }
        response.setHosts(hosts);

        List<Casting> findCastings = castingRepository.findByEventId(findEvent.getId());
        List<String> castings = new ArrayList<>();
        for (Casting findCasting : findCastings) {
            castings.add(findCasting.getCastingName());
        }
        response.setCastings(castings);

        List<Image> findImages = imageService.findByEventId(findEvent.getId());
        List<String> imageUrls = new ArrayList<>();
        for (Image findImage : findImages) {
            if (findImage.getIsThumbnail()) {
                response.setThumbNailUrl(findImage.getS3Url());
            } else {
                imageUrls.add(findImage.getS3Url());
            }
        }
        response.setImageUrls(imageUrls);

        // 조회수 증가
        findEvent.updateViewCount();
        return response;
    }

    @Override
    public MainPageDto getMainPage() {
        MainPageDto mainPage = eventRepository.getMainPage();
        return mainPage;
    }

    @Override
    public Page<EventListResponse> findAllEvent(int page, int size) {
        // todo: 조건이 추가될 것 같음 and 테스트코드 작성
        Page<Event> eventListPage = eventRepository.findAllEventsPage(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startEvent")));
        List<EventListResponse> eventListResponseList = eventListPage.stream()
                .map(e -> EventListResponse.from(e))
                .collect(Collectors.toList());
        return new PageImpl<>(eventListResponseList, eventListPage.getPageable(), eventListPage.getTotalElements());
    }

    @Override
    public void cancelEventByEventId(Long eventId, Long memberId) {
        Event findEvent = eventRepository.findByIdFetchAll(eventId);

        if (findEvent.getMember().getId() != memberId) {
            throw new CustomException(ErrorCode.EVENT_CANCEL_FAIL);
        }

        LocalDateTime startEvent = findEvent.getStartEvent();
        LocalDateTime now = LocalDateTime.now();
        List<Ticketing> findTicketings = ticketingRepository.findByEventId(findEvent.getId());
        if (findTicketings.size() == 0 && startEvent.isAfter(now.plus(7, ChronoUnit.DAYS))) {
            findEvent.cancelEvent();
        } else {
            throw new CustomException(ErrorCode.EVENT_CANCEL_FAIL);
        }
    }

    //== graphql 에 사용 ==//
    @Override
    public List<Event> getEventByPlace(String place) {
        return eventRepository.findByPlace(place);
    }
    @Override
    public List<Event> getEventByName(String name) {
        return eventRepository.findByName(name);
    }
}

