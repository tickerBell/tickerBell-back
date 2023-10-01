package com.tickerBell.domain.tag.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.tag.entity.Tag;
import com.tickerBell.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
}
