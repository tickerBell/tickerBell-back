package com.tickerBell.domain.tag.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.tag.entity.Tag;
import com.tickerBell.domain.tag.repository.TagRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long saveTag(String tagName, Long eventId, Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Event findEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomException(ErrorCode.EVENT_NOT_FOUND)
        );

        Tag tag = Tag.builder()
                .tagName(tagName)
                .event(findEvent)
                .member(findMember)
                .build();
        Tag savedTag = tagRepository.save(tag);

        return savedTag.getId();
    }
}
