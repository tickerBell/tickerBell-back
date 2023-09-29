package com.tickerBell.domain.category.service;

import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.category.repository.CategoryRepository;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
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
}
