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
import com.tickerBell.domain.tag.service.TagService;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import com.tickerBell.global.graphql.dtos.EventGraphqlResponse;
import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    @Mock
    private TicketingRepository ticketingRepository;
    @Mock
    private ImageRepository imageRepository;
//    private static MockedStatic<EventListResponse> eventListResponseMockedStatic; // static 메소드 mocking
//
//    @BeforeEach
//    void setUp() {
//        eventListResponseMockedStatic = Mockito.mockStatic(EventListResponse.class);
//    }
//
//    @AfterEach
//    void afterEach() {
//        // static 을 mocking 할 경우 다른 테스트 케이스에도 영향을 미치기 때문에 close
//        eventListResponseMockedStatic.close();
//    }


    @Test
    @DisplayName("이벤트 저장 테스트")
    void saveEventTest() {
        // given
        Long memberId = 1L;
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        LocalDateTime availablePurchaseTime = LocalDateTime.now();
        LocalTime dailyStartEvent = LocalTime.now();
        Integer eventTime = 90;
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
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        SaveEventRequest saveEventRequest = new SaveEventRequest(name, startEvent, endEvent, dailyStartEvent, eventTime, availablePurchaseTime, normalPrice, premiumPrice, saleDegree, castings, hosts, place, isAdult, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, category, tags, imageUrls);
        Event event = Event.builder().build();

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
        when(specialSeatService.saveSpecialSeat(anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(SpecialSeat.builder().build());
        when(tagService.saveTagList(any(List.class))).thenReturn(1);
        when(hostRepository.save(any(Host.class))).thenReturn(Host.builder().event(event).build());
        when(castingRepository.save(any(Casting.class))).thenReturn(Casting.builder().event(event).build());
        doNothing().when(imageService).updateEventByImageUrl("url1", null);

        when(eventRepository.save(any(Event.class))).thenReturn(event);


        // when
        Long savedEventId = eventService.saveEvent(memberId, saveEventRequest);

        // then
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(specialSeatService, times(1)).saveSpecialSeat(anyBoolean(), anyBoolean(), anyBoolean());
        verify(tagService, times(1)).saveTagList(any(List.class));
        verify(hostRepository, times(2)).save(any(Host.class));
        verify(castingRepository, times(2)).save(any(Casting.class));
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(imageService, times(1)).updateEventByImageUrl("url1", null);
    }

    @Test
    @DisplayName("이벤트 저장 회원조회 실패 테스트")
    void saveEventMemberFailTest() {
        // given
        Long memberId = 1L;
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        LocalTime dailyStartEvent = LocalTime.now();
        LocalDateTime availablePurchaseTime = LocalDateTime.now();
        Integer eventTime = 90;
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
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        imageUrls.add("url2");
        SaveEventRequest saveEventRequest = new SaveEventRequest(name, startEvent, endEvent, dailyStartEvent, eventTime, availablePurchaseTime, normalPrice, premiumPrice, saleDegree, castings, hosts, place, isAdult, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, category, null, imageUrls);

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
        LocalTime dailyStartEvent = LocalTime.now();
        Integer eventTime = 90;
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
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("url1");
        imageUrls.add("url2");
        List<MultipartFile> eventImages = new ArrayList<>();
        eventImages.add(new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]));
        eventImages.add(new MockMultipartFile("image2.png", "image2.png", "image/png", new byte[0]));
        SaveEventRequest saveEventRequest = new SaveEventRequest(name, startEvent, endEvent, dailyStartEvent, eventTime, availablePurchaseTime, normalPrice, premiumPrice, saleDegree, castings, hosts, place, isAdult, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, category, tags, imageUrls);

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
        Event event = Event.builder().normalPrice(1000).premiumPrice(-1).saleDegree(0.5F).build();
        events.add(event);
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<Event> eventsPage = new PageImpl<>(events, pageRequest, 10);
        Image image = Image.builder().s3Url("s3Url").build();
        Casting.builder().castingName("casting").event(event).build();

        // stub
        when(eventRepository.findByCategoryFetchAllPage(category, pageRequest)).thenReturn(eventsPage);
        when(imageRepository.findThumbNailImageByEventId(null)).thenReturn(image);

        // when
        EventCategoryResponse eventCategoryResponse = eventService.getEventByCategory(category, pageRequest);

        // then
        assertThat(eventCategoryResponse.getEventListResponses()).isNotEmpty();
        verify(eventRepository, times(1)).findByCategoryFetchAllPage(category, pageRequest);
    }

    @Test
    @DisplayName("이벤트 PK 조회 테스트")
    void findByIdFetchAllTest() {
        // given
        Event mockEvent = createMockEvent(createMockMember(), createMockSpecialSeat(), 0.1F);
        Long eventId = 1L;
        List<Host> hosts = new ArrayList<>();
        List<Casting> castings = new ArrayList<>();
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            hosts.add(Host.builder().hostName("host").event(mockEvent).build());
            castings.add(Casting.builder().castingName("casting").event(mockEvent).build());
        }
        images.add(Image.builder().isThumbnail(true).s3Url("url").build());
        images.add(Image.builder().isThumbnail(false).s3Url("url").build());
        images.get(0).addEvent(mockEvent);
        images.get(1).addEvent(mockEvent);

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
        assertThat(eventResponse.getHosts().size()).isEqualTo(2);
        assertThat(eventResponse.getCastings().size()).isEqualTo(2);
        assertThat(eventResponse.getThumbNailUrl()).isEqualTo("url");
        assertThat(eventResponse.getImageUrls().size()).isEqualTo(1);
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
                Event mockEvent = createMockEvent(createMockMember(), createMockSpecialSeat(), 0F);
                Long eventId = 1L;

                // stub
                when(eventRepository.findByIdFetchAll(eventId)).thenReturn(mockEvent);

                // when
                EventResponse eventResponse = eventService.findByIdFetchAll(eventId);

                // then
                assertThat(eventResponse.getDiscountNormalPrice()).isEqualTo(mockEvent.getNormalPrice().floatValue());
            } else {
                Event mockEvent = createMockEvent(createMockMember(), createMockSpecialSeat(), 0F);
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

    @Test
    @DisplayName("이벤트 취소 테스트")
    void cancelEventByEventIdTest() {
        // given
        Long eventId = 1L;
        Long memberId = 1L;
        Member member = Member.builder().build();
        Event event = Event.builder().startEvent(LocalDateTime.now().plusDays(8)).member(member).build();
        List<Ticketing> ticketings = new ArrayList<>();

        // stub
        when(eventRepository.findByIdFetchAll(eventId)).thenAnswer(invocation -> {
            setPrivateField(member, "id", 1L);
            return event;
        });
        when(ticketingRepository.findByEventId(null)).thenReturn(ticketings);

        // when
        eventService.cancelEventByEventId(eventId, memberId);

        // then
        assertThat(event.getIsCancelled()).isTrue();
    }

    @Test
    @DisplayName("이벤트 취소 내가 등록한 이벤트가 아닐 때 실패 테스트")
    void cancelEventByEventIdMemberFailTest() {
        // given
        Long eventId = 1L;
        Long memberId = 2L;
        Member member = Member.builder().build();
        Event event = Event.builder().startEvent(LocalDateTime.now().plusDays(8)).member(member).build();
        List<Ticketing> ticketings = new ArrayList<>();

        // stub
        when(eventRepository.findByIdFetchAll(eventId)).thenAnswer(invocation -> {
            setPrivateField(member, "id", 1L);
            return event;
        });

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> eventService.cancelEventByEventId(eventId, memberId))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EVENT_CANCEL_FAIL);
        });
        assertThat(event.getIsCancelled()).isFalse();
    }

    @Test
    @DisplayName("이벤트 취소 예매내역 실패 테스트")
    void cancelEventByEventIdTicketingFailTest() {
        // given
        Long eventId = 1L;
        Long memberId = 1L;
        Member member = Member.builder().build();
        Event event = Event.builder().startEvent(LocalDateTime.now().plusDays(8)).member(member).build();
        List<Ticketing> ticketings = new ArrayList<>();
        ticketings.add(Ticketing.builder().build());

        // stub
        when(eventRepository.findByIdFetchAll(eventId)).thenAnswer(invocation -> {
            setPrivateField(member, "id", 1L);
            return event;
        });
        when(ticketingRepository.findByEventId(null)).thenReturn(ticketings);

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> eventService.cancelEventByEventId(eventId, memberId))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EVENT_CANCEL_FAIL);
        });
        assertThat(event.getIsCancelled()).isFalse();
    }

    @Test
    @DisplayName("이벤트 취소 남은 날짜 실패 테스트")
    void cancelEventByEventIdDayFailTest() {
        // given
        Long eventId = 1L;
        Long memberId = 1L;
        Member member = Member.builder().build();
        Event event = Event.builder().startEvent(LocalDateTime.now().plusDays(6)).member(member).build();
        List<Ticketing> ticketings = new ArrayList<>();
        ticketings.add(Ticketing.builder().build());

        // stub
        when(eventRepository.findByIdFetchAll(eventId)).thenAnswer(invocation -> {
            setPrivateField(member, "id", 1L);
            return event;
        });

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> eventService.cancelEventByEventId(eventId, memberId))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EVENT_CANCEL_FAIL);
        });
        assertThat(event.getIsCancelled()).isFalse();
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

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    @DisplayName("메인 페이지 반환 테스트")
    public void mainPageTest() {
        // given
        MainPageDto mainPageDto = MainPageDto.builder().build();
        Event spyEvent = spy(Event.builder().saleDegree(1000F).normalPrice(10000).premiumPrice(15000).build());
        List<Image> imageList = List.of(Image.builder().event(spyEvent).isThumbnail(true).build());
        List<Casting> castingList = List.of(Casting.builder().event(spyEvent).castingName("casting").build());
        List<Event> eventList = List.of(spyEvent);

        // stub
        when(spyEvent.getCastingList()).thenReturn(castingList);
        when(spyEvent.getImageList()).thenReturn(imageList);
        when(eventRepository.findByCategoryInMainPage(any(Category.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(eventList);
        when(eventRepository.findBySaleInMainPage(any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(eventList);
        when(eventRepository.findByDeadLineInMainPage(any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(eventList);

        // when
        MainPageDto mainPage = eventService.getMainPage();

        // then
        verify(eventRepository, times(4)).findByCategoryInMainPage(any(Category.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(eventRepository, times(1)).findBySaleInMainPage(any(LocalDateTime.class), any(PageRequest.class));
        verify(eventRepository, times(1)).findByDeadLineInMainPage(any(LocalDateTime.class), any(PageRequest.class));

    }

    @Test
    @DisplayName("모든 이벤트 조회 테스트")
    public void allEventFindTest() {
        // given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startEvent"));
        Event mockEvent = mock(Event.class);
        Image mockImage = mock(Image.class);
        Casting casting = Casting.builder().event(mockEvent).build();
        List<Event> eventEntities = List.of(mockEvent);
        Page<Event> eventPage = new PageImpl<>(eventEntities);

        // stub
        when(mockEvent.getCastingList()).thenReturn(List.of(casting));
        when(mockEvent.getImageList()).thenReturn(List.of(mockImage));
        when(mockEvent.getSaleDegree()).thenReturn(1000F);
        when(mockImage.getIsThumbnail()).thenReturn(true);
        when(eventRepository.findAllEventsPage(pageRequest)).thenReturn(eventPage);

        // when
        Page<EventListResponse> eventListResponseList = eventService.findAllEvent(page, size);

        // then
        verify(eventRepository, times(1)).findAllEventsPage(any(PageRequest.class));
        assertThat(eventListResponseList.getTotalElements()).isEqualTo(eventEntities.size());
    }

    @Test
    @DisplayName("graphql 장소 검색 테스트")
    public void getEventByPlaceTest() {
        // given
        String place = "place";
        List<Event> eventList = List.of(Event.builder().build());
        EventGraphqlResponse eventGraphqlResponse = EventGraphqlResponse.builder().build();

        // static 메소드 mocking
        MockedStatic<EventGraphqlResponse> eventGraphqlResponseMockedStatic = mockStatic(EventGraphqlResponse.class);


        // stub
        when(eventRepository.findByPlace(any(String.class))).thenReturn(eventList);
        given(EventGraphqlResponse.from(any())).willReturn(eventGraphqlResponse);

        // when
        List<EventGraphqlResponse> eventGraphqlResponseList = eventService.getEventByPlace(place);

        // then
        verify(eventRepository, times(1)).findByPlace(any(String.class));
        assertThat(eventGraphqlResponseList.size()).isEqualTo(eventList.size());

        eventGraphqlResponseMockedStatic.close();
    }

    @Test
    @DisplayName("graphql 이벤트명 검색 테스트")
    public void getEventByNameTest() {
        // given
        String name = "name";
        List<Event> eventList = List.of(Event.builder().build());
        EventGraphqlResponse eventGraphqlResponse = EventGraphqlResponse.builder().build();

        // static 메소드 mocking
        MockedStatic<EventGraphqlResponse> eventGraphqlResponseMockedStatic = mockStatic(EventGraphqlResponse.class);

        // stub
        when(eventRepository.findByName(any(String.class))).thenReturn(eventList);
        given(EventGraphqlResponse.from(any())).willReturn(eventGraphqlResponse);

        // when
        List<EventGraphqlResponse> eventByName = eventService.getEventByName(name);

        // then
        verify(eventRepository, times(1)).findByName(any(String.class));
        assertThat(eventByName.size()).isEqualTo(eventList.size());

        eventGraphqlResponseMockedStatic.close();
    }

    @Test
    @DisplayName("graphql 캐스팅 정보로 검색")
    public void getEventByCastingTest() {
        // given
        String casting = "casting";
        List<Event> eventList = List.of(Event.builder().build());
        EventGraphqlResponse eventGraphqlResponse = EventGraphqlResponse.builder().build();

        // static 메소드 mocking
        MockedStatic<EventGraphqlResponse> eventGraphqlResponseMockedStatic = mockStatic(EventGraphqlResponse.class);

        // stub
        when(eventRepository.findByCasting(any(String.class))).thenReturn(eventList);
        given(EventGraphqlResponse.from(any())).willReturn(eventGraphqlResponse);

        // when
        List<EventGraphqlResponse> eventByName = eventService.getEventByCasting(casting);

        // then
        verify(eventRepository, times(1)).findByCasting(any(String.class));
        assertThat(eventByName.size()).isEqualTo(eventList.size());

        eventGraphqlResponseMockedStatic.close();
    }
}
