//package com.tickerBell.global;
//
//import com.tickerBell.domain.casting.entity.Casting;
//import com.tickerBell.domain.event.entity.Category;
//import com.tickerBell.domain.event.entity.Event;
//import com.tickerBell.domain.host.entity.Host;
//import com.tickerBell.domain.image.entity.Image;
//import com.tickerBell.domain.member.entity.AuthProvider;
//import com.tickerBell.domain.member.entity.Member;
//import com.tickerBell.domain.member.entity.Role;
//import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
//import com.tickerBell.domain.specialseat.entity.SpecialSeat;
//import com.tickerBell.domain.ticketing.entity.Ticketing;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class InitDB {
//    private final InitService initService;
//
//    public static long time;
//
//    @PostConstruct
//    public void init() {
//        initService.initMember();
//    }
//
//    @Service
//    @RequiredArgsConstructor
//    @Transactional
//    public static class InitService {
//        private final EntityManager em;
//        private final BCryptPasswordEncoder encoder;
//
//        public void initMember() {
//            Member member1 = Member.builder().username("abcdefg").password(encoder.encode("abcdefg1")).phone("01031725949").isAdult(true)
//                    .role(Role.ROLE_REGISTRANT).authProvider(AuthProvider.KAKAO).build();
//            em.persist(member1);
//
//            createEvent(member1, "겨울, 우리 함께 황영웅 첫 번째 팬 콘서트", Category.CONCERT, "경기 고양시 일산서구 킨텍스로 217-60 (대화동 2600)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "태양의 서커스", Category.CONCERT, "서울특별시 송파구 올림픽로 25 (잠실동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "렌트", Category.CONCERT, "서울특별시 강남구 영동대로513(삼성동, 코엑스)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "베르사유의 장미", Category.CONCERT, "서울시 강서구 마곡중앙로 136", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "쇼뮤지컬 와일드 와일드", Category.CONCERT, "서울특별시 중구 마른내로 47(초동) 지하 1층", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");createEvent(member1, "겨울, 우리 함께 황영웅 첫 번째 팬 콘서트", Category.CONCERT, "경기 고양시 일산서구 킨텍스로 217-60 (대화동 2600)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "태양의 서커스", Category.CONCERT, "서울특별시 송파구 올림픽로 25 (잠실동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "렌트", Category.CONCERT, "서울특별시 강남구 영동대로513(삼성동, 코엑스)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "베르사유의 장미", Category.CONCERT, "서울시 강서구 마곡중앙로 136", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//            createEvent(member1, "쇼뮤지컬 와일드 와일드", Category.CONCERT, "서울특별시 중구 마른내로 47(초동) 지하 1층", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EA%B2%A8%EC%9A%B8%2C%EC%9A%B0%EB%A6%AC%ED%95%A8%EA%BB%98.png");
//
//            createEvent(member1, "쉬어매드니스 - 대학로 1위 연극", Category.PLAY, "서울시 종로구 동숭동 1-145번지 대학빌딩 지하1층", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "옥탑방 고양이", Category.PLAY, "서울특별시 마포구 신수동 1번지 서강대학교 커뮤니케이션센터 메리홀 105호", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "네이처 오브 포겟팅", Category.PLAY, "서울특별시 중구 명동1가 54번지", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "고도를 기다리며", Category.PLAY, "서울 중구 장충단로 59", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "참기름아저씨", Category.PLAY, "서울특별시 종로구 성균관로 87(명륜1가) 그라운드씬", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "쉬어매드니스 - 대학로 1위 연극", Category.PLAY, "서울시 종로구 동숭동 1-145번지 대학빌딩 지하1층", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "옥탑방 고양이", Category.PLAY, "서울특별시 마포구 신수동 1번지 서강대학교 커뮤니케이션센터 메리홀 105호", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "네이처 오브 포겟팅", Category.PLAY, "서울특별시 중구 명동1가 54번지", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "고도를 기다리며", Category.PLAY, "서울 중구 장충단로 59", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//            createEvent(member1, "참기름아저씨", Category.PLAY, "서울특별시 종로구 성균관로 87(명륜1가) 그라운드씬", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%89%AC%EC%96%B4%EB%A7%A4%EB%93%9C%EB%8B%88%EC%8A%A4.png");
//
//
//            createEvent(member1, "스즈메의 문단속 공식 필름 콘서트", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "문화릴레이티켓", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "히사이시 조", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "프라하 심포니", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "콘체르토 마라톤", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "스즈메의 문단속 공식 필름 콘서트", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "문화릴레이티켓", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "히사이시 조", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "프라하 심포니", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//            createEvent(member1, "콘체르토 마라톤", Category.CLASSIC, "서울특별시 서대문구 신촌동 134 연세대학교 백주년기념관", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EC%8A%A4%EC%A6%88%EB%A9%94%EC%9D%98%EB%AC%B8%EB%8B%A8%EC%86%8D.png");
//
//
//            createEvent(member1, "레미제라블", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "스모크", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "리진 : 빛의 여인", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "문스토리", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "안테모사", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "레미제라블", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "스모크", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "리진 : 빛의 여인", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "문스토리", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//            createEvent(member1, "안테모사", Category.MUSICAL, "서울특별시 용산구 이태원로 294 블루스퀘어(한남동)", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%A0%88%EB%AF%B8%EC%A0%9C%EB%9D%BC%EB%B8%94.png");
//
//
//            createEvent(member1, "수원삼성블루윙즈", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "강원 FC", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "성남 FC", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "부천 FC 1995", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "서울이랜드 FC", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "수원삼성블루윙즈", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "강원 FC", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "성남 FC", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "부천 FC 1995", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//            createEvent(member1, "서울이랜드 FC", Category.SPORTS, "서울 송파구 올림픽로 19-2 서울종합운동장", "https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/%EB%91%90%EC%82%B0.png");
//
//        }
//
//        private void createEvent(Member member1, String eventName, Category category, String place, String s3Url) {
//            SpecialSeat specialSeat = SpecialSeat.builder()
//                    .isSpecialSeatA(true)
//                    .isSpecialSeatB(true)
//                    .isSpecialSeatC(true)
//                    .build();
//            em.persist(specialSeat);
//            Event event = Event.builder()
//                    .name(eventName)
//                    .startEvent(LocalDateTime.now().plusDays(10).plusHours(1))
//                    .endEvent(LocalDateTime.now().plusDays(10).plusHours(3))
//                    .availablePurchaseTime(LocalDateTime.now().plusDays(10))
//                    .eventTime(100)
//                    .normalPrice(10000)
//                    .premiumPrice(15000)
//                    .saleDegree(2000F)
//                    .totalSeat(60)
//                    .isAdult(true)
//                    .remainSeat(60)
//                    .place(place)
//                    .category(category)
//                    .member(member1)
//                    .specialSeat(specialSeat)
//                    .build();
//            em.persist(event);
//
//            Casting casting = Casting.builder().event(event).castingName("배우").build();
//            em.persist(casting);
//
//            Host host = Host.builder().hostName("주최자").event(event).build();
//            em.persist(host);
//
//            Image image = Image.builder().s3Url(s3Url).storeImgName("storeImgName")
//                    .isThumbnail(true).originImgName("originalName").event(event).build();
//            em.persist(image);
//        }
//    }
//}
