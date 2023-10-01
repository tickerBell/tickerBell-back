package com.tickerBell.domain.tag.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.tag.entity.Tag;
import com.tickerBell.domain.tag.repository.TagRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("태그 저장 테스트")
    void saveTagTest() {
        // given
        String tagName = "mockTagName";
        Long memberId = 1L;
        Long eventId = 1L;

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(Event.builder().build()));
        when(tagRepository.save(any(Tag.class))).thenReturn(Tag.builder().build());

        // when
        tagService.saveTag(tagName, eventId, memberId);

        // then
        verify(memberRepository, times(1)).findById(memberId);
        verify(eventRepository, times(1)).findById(eventId);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    @DisplayName("태그 저장 회원조회 실패 테스트")
    void saveTagMemberFailTest() {
        // given
        String tagName = "mockTagName";
        Long memberId = 1L;
        Long eventId = 1L;

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> tagService.saveTag(tagName, eventId, memberId))
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
    @DisplayName("태그 저장 이벤트 조회 실패 테스트")
    void saveTagEventFailTest() {
        // given
        String tagName = "mockTagName";
        Long memberId = 1L;
        Long eventId = 1L;

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> tagService.saveTag(tagName, eventId, memberId))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EVENT_NOT_FOUND);
            assertThat(ex.getStatus()).isEqualTo(ErrorCode.EVENT_NOT_FOUND.getStatus().toString());
            assertThat(ex.getErrorMessage()).isEqualTo(ErrorCode.EVENT_NOT_FOUND.getErrorMessage());
        });
    }
}
