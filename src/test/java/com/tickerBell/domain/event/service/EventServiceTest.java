package com.tickerBell.domain.event.service;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.casting.repository.CastingRepository;
import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.EventResponse;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.host.repository.HostRepository;
import com.tickerBell.domain.image.service.ImageService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.specialseat.service.SpecialSeatService;
import com.tickerBell.domain.tag.service.TagService;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SpecialSeatService specialSeatService;
    @Mock
    private TagService tagService;
    @Mock
    private HostRepository hostRepository;
    @Mock
    private CastingRepository castingRepository;
    @Mock
    private ImageService imageService;

    @Test
    @DisplayName("이벤트 저장 테스트")
    void saveEventTest() {
        // given
        Long memberId = 1L;
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        LocalDateTime availablePurchaseTime = LocalDateTime.now();
        Integer normalPrice = 100;
        Integer premiumPrice = 1000;
        Float saleDegree = 0.0F;
        String place = "mockPlace";
        Boolean isAdult = true;
        Category category = Category.PLAY;
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        List<String> hosts = new ArrayList<>();
        hosts.add("host1");
        hosts.add("host2");
        List<String> castings = new ArrayList<>();
        castings.add("casting1");
        castings.add("casting2");
        MockMultipartFile thumbNailImage = new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]);
        List<MultipartFile> eventImages = new ArrayList<>();
        eventImages.add(new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]));
        eventImages.add(new MockMultipartFile("image2.png", "image2.png", "image/png", new byte[0]));
        SaveEventRequest saveEventRequest = new SaveEventRequest(name, startEvent, endEvent, availablePurchaseTime, normalPrice, premiumPrice, saleDegree, castings, hosts, place, isAdult, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, category, tags, thumbNailImage, eventImages);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
        when(specialSeatService.saveSpecialSeat(anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(SpecialSeat.builder().build());
        when(tagService.saveTagList(any(List.class))).thenReturn(1);
        when(hostRepository.save(any(Host.class))).thenReturn(Host.builder().build());
        when(castingRepository.save(any(Casting.class))).thenReturn(Casting.builder().build());
        when(imageService.uploadImage(thumbNailImage, eventImages)).thenReturn(new ArrayList<>());

        when(eventRepository.save(any(Event.class))).thenReturn(Event.builder().build());


        // when
        Long savedEventId = eventService.saveEvent(memberId, saveEventRequest);

        // then
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(specialSeatService, times(1)).saveSpecialSeat(anyBoolean(), anyBoolean(), anyBoolean());
        verify(tagService, times(1)).saveTagList(any(List.class));
        verify(hostRepository, times(2)).save(any(Host.class));
        verify(castingRepository, times(2)).save(any(Casting.class));
        verify(imageService, times(1)).uploadImage(thumbNailImage, eventImages);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("이벤트 저장 회원조회 실패 테스트")
    void saveEventMemberFailTest() {
        // given
        Long memberId = 1L;
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        LocalDateTime availablePurchaseTime = LocalDateTime.now();
        Integer normalPrice = 100;
        Integer premiumPrice = 1000;
        Float saleDegree = 0.0F;
        List<String> castings = new ArrayList<>();
        castings.add("casting1");
        castings.add("casting2");
        List<String> hosts = new ArrayList<>();
        hosts.add("host1");
        hosts.add("host2");
        String place = "mockPlace";
        Boolean isAdult = true;
        Category category = Category.SPORTS;
        MockMultipartFile thumbNailImage = new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]);
        List<MultipartFile> eventImages = new ArrayList<>();
        eventImages.add(new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]));
        eventImages.add(new MockMultipartFile("image2.png", "image2.png", "image/png", new byte[0]));
        SaveEventRequest saveEventRequest = new SaveEventRequest(name, startEvent, endEvent, availablePurchaseTime, normalPrice, premiumPrice, saleDegree, castings, hosts, place, isAdult, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, category, null, thumbNailImage, eventImages);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(
                () -> eventService.saveEvent(memberId, saveEventRequest))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
            assertThat(ex.getStatus()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getStatus().toString());
            assertThat(ex.getErrorMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getErrorMessage());
        });
    }

    @Test
    @DisplayName("구매 가능 시간이 null 일 경우 테스트")
    void saveEventPurchaseTimeNullTest() {
        Long memberId = 1L;
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        LocalDateTime availablePurchaseTime =  null;
        Integer normalPrice = 100;
        Integer premiumPrice = 1000;
        Float saleDegree = 0.0F;
        List<String> castings = new ArrayList<>();
        castings.add("casting1");
        castings.add("casting2");
        List<String> hosts = new ArrayList<>();
        hosts.add("host1");
        hosts.add("host2");
        String place = "mockPlace";
        Boolean isAdult = true;
        Category category = Category.PLAY;
        List<String> tags = new ArrayList<>();
        MockMultipartFile thumbNailImage = new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]);
        List<MultipartFile> eventImages = new ArrayList<>();
        eventImages.add(new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]));
        eventImages.add(new MockMultipartFile("image2.png", "image2.png", "image/png", new byte[0]));
        SaveEventRequest saveEventRequest = new SaveEventRequest(name, startEvent, endEvent, availablePurchaseTime, normalPrice, premiumPrice, saleDegree, castings, hosts, place, isAdult, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, category, tags, thumbNailImage, eventImages);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
        when(specialSeatService.saveSpecialSeat(anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(SpecialSeat.builder().build());
        when(tagService.saveTagList(any(List.class))).thenReturn(1);
        when(eventRepository.save(any(Event.class))).thenReturn(Event.builder().build());

        // when
        Long savedEventId = eventService.saveEvent(memberId, saveEventRequest);

        // then
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("카테고리를 이용한 이벤트 조회 테스트")
    void getEventByCategoryTest() {
        // given
        Category category = Category.SPORTS;
        List<Event> events = new ArrayList<>();
        events.add(Event.builder().build());

        // stub
        when(eventRepository.findByCategory(category)).thenReturn(events);

        // when
        List<EventListResponse> eventsResponse = eventService.getEventByCategory(category);

        // then
        assertThat(eventsResponse).isNotEmpty();
        verify(eventRepository, times(1)).findByCategory(category);
    }

    @Test
    @DisplayName("이벤트 PK 조회 테스트")
    void findByIdFetchAllTest() {
        // given
        Event mockEvent = createMockEvent(createMockMember(), createMockSpecialSeat(), 0.1F);
        Long eventId = 1L;

        // stub
        when(eventRepository.findByIdFetchAll(eventId)).thenReturn(mockEvent);

        // when
        EventResponse eventResponse = eventService.findByIdFetchAll(eventId);

        // then
        assertThat(eventResponse.getName()).isEqualTo(mockEvent.getName());
        assertThat(eventResponse.getStartEvent()).isEqualTo(mockEvent.getStartEvent());
        assertThat(eventResponse.getEndEvent()).isEqualTo(mockEvent.getEndEvent());
        assertThat(eventResponse.getNormalPrice()).isEqualTo(mockEvent.getNormalPrice());
        assertThat(eventResponse.getPremiumPrice()).isEqualTo(mockEvent.getPremiumPrice());
        assertThat(eventResponse.getDiscountNormalPrice()).isEqualTo(mockEvent.getNormalPrice() - ((Float) (mockEvent.getNormalPrice() * mockEvent.getSaleDegree())));
        assertThat(eventResponse.getDiscountPremiumPrice()).isEqualTo(mockEvent.getPremiumPrice() - ((Float) (mockEvent.getPremiumPrice() * mockEvent.getSaleDegree())));
        assertThat(eventResponse.getPlace()).isEqualTo(mockEvent.getPlace());
        assertThat(eventResponse.getIsAdult()).isEqualTo(mockEvent.getIsAdult());
        assertThat(eventResponse.getCategory()).isEqualTo(mockEvent.getCategory());
        assertThat(eventResponse.getIsSpecialSeatA()).isEqualTo(mockEvent.getSpecialSeat().getIsSpecialSeatA());
        assertThat(eventResponse.getIsSpecialSeatB()).isEqualTo(mockEvent.getSpecialSeat().getIsSpecialSeatB());
        assertThat(eventResponse.getIsSpecialSeatC()).isEqualTo(mockEvent.getSpecialSeat().getIsSpecialSeatC());
    }

    @Test
    @DisplayName("discount 메서드 분기 테스트")
    void discountTest() {
        // given
        for (int i = 0; i < 3; i++) {

            if (i == 0) {
                Event mockEvent = createMockEvent(createMockMember(), createMockSpecialSeat(), 1000F);
                Long eventId = 1L;

                // stub
                when(eventRepository.findByIdFetchAll(eventId)).thenReturn(mockEvent);

                // when
                EventResponse eventResponse = eventService.findByIdFetchAll(eventId);

                // then
                assertThat(eventResponse.getDiscountNormalPrice()).isEqualTo(mockEvent.getNormalPrice() - mockEvent.getSaleDegree());
            } else if (i == 1) {
                Event mockEvent = createMockEvent(createMockMember(), createMockSpecialSeat(), -1F);
                Long eventId = 1L;

                // stub
                when(eventRepository.findByIdFetchAll(eventId)).thenReturn(mockEvent);

                // when
                EventResponse eventResponse = eventService.findByIdFetchAll(eventId);

                // then
                assertThat(eventResponse.getDiscountNormalPrice()).isEqualTo(mockEvent.getNormalPrice().floatValue());
            } else {
                Event mockEvent = createMockEvent(createMockMember(), createMockSpecialSeat(), null);
                Long eventId = 1L;

                // stub
                when(eventRepository.findByIdFetchAll(eventId)).thenReturn(mockEvent);

                // when
                EventResponse eventResponse = eventService.findByIdFetchAll(eventId);

                // then
                assertThat(eventResponse.getDiscountNormalPrice()).isEqualTo(mockEvent.getNormalPrice().floatValue());
            }
        }

    }

    private Member createMockMember() {
        return Member.builder().build();
    }

    private SpecialSeat createMockSpecialSeat() {
        return SpecialSeat.builder()
                .isSpecialSeatA(true)
                .isSpecialSeatB(true)
                .isSpecialSeatC(true)
                .build();
    }

    private Event createMockEvent(Member member, SpecialSeat specialSeat, Float saleDegree) {
        return Event.builder()
                .name("name")
                .startEvent(LocalDateTime.now())
                .endEvent(LocalDateTime.now())
                .normalPrice(10000)
                .premiumPrice(15000)
                .saleDegree(saleDegree)
                .totalSeat(60)
                .remainSeat(60)
                .place("place")
                .isAdult(true)
                .category(Category.CONCERT)
                .member(member)
                .specialSeat(specialSeat)
                .build();
    }
}
