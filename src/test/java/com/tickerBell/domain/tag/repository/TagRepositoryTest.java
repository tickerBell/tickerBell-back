package com.tickerBell.domain.tag.repository;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.tag.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("태그 저장 테스트")
    void saveTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        Event event = Event.builder().member(savedMember).build();
        Event savedEvent = eventRepository.save(event);
        String tagName = "tag";
        Tag tag = Tag.builder().tagName(tagName).member(savedMember).event(event).build();

        // when
        Tag savedTag = tagRepository.save(tag);

        // then
        assertThat(savedTag).isEqualTo(tag);
        assertThat(savedTag.getId()).isEqualTo(tag.getId());
        assertThat(savedTag.getTagName()).isEqualTo(tag.getTagName()).isEqualTo(tagName);
        assertThat(savedTag.getMember()).isEqualTo(tag.getMember());
        assertThat(savedTag.getEvent()).isEqualTo(tag.getEvent());
    }
}