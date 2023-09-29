package com.tickerBell.domain.category.service;

import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.category.repository.CategoryRepository;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
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
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private EventRepository eventRepository;

    @Test
    @DisplayName("카테고리 저장 테스트")
    void saveCategoryTest() {
        // given
        Long eventId = 1L;
        Categories categories = Categories.PLAY;

        // stub
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(Event.builder().build()));
        when(categoryRepository.save(any(Category.class))).thenReturn(Category.builder().build());

        // when
        categoryService.saveCategory(eventId, categories);

        // then
        verify(eventRepository, times(1)).findById(eventId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("카테고리 저장 이벤트 조회 실패 테스트")
    void saveCategoryEventFailTest() {
        // given
        Long eventId = 1L;
        Categories categories = Categories.CLASSIC;

        // stub
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> categoryService.saveCategory(eventId, categories))
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
