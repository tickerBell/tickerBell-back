package com.tickerBell.domain.category.service;

import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.category.repository.CategoryRepository;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.assertj.core.api.AbstractObjectAssert;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import com.tickerBell.domain.category.dtos.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

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

    @DisplayName("이벤트에 해당하는 카테고리 조회")
    @Test
    public void testGetCategoryByEvent() {
        // given
        Event event = Event.builder().id(1L).build();
        List<Category> categoryList = new ArrayList<>();
        Category category1 = Category.builder().name("category1").event(event).build();
        Category category2 = Category.builder().name("category2").event(event).build();
        categoryList.add(category1);
        categoryList.add(category2);


        // stub
        when(categoryRepository.findByEventId(event.getId())).thenReturn(categoryList);

        // when
        List<CategoryResponse> categoryResponseList = categoryService.getCategoryListByEvent(event.getId());

        // then
        verify(categoryRepository, times(1)).findByEventId(any());
        assertEquals(categoryResponseList.size(), categoryList.size());
        assertEquals(categoryResponseList.get(0).getName(), "category1");
        assertEquals(categoryResponseList.get(1).getName(), "category2");
    }

    @DisplayName("카테고리 전체 조회")
    @Test
    public void testGetAllCategory() {
        // given
        List<Category> categoryList = new ArrayList<>();
        Category category1 = Category.builder().name("category1").build();
        Category category2 = Category.builder().name("category2").build();
        Category category3 = Category.builder().name("category3").build();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        // stub
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // when
        List<CategoryResponse> categoryResponseList = categoryService.getAllCategory();

        // then
        verify(categoryRepository, times(1)).findAll();
        assertEquals(categoryResponseList.size(), categoryList.size());
        assertEquals(categoryResponseList.get(0).getName(), "category1");
        assertEquals(categoryResponseList.get(1).getName(), "category2");
    }
}
