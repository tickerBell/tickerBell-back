package com.tickerBell.domain.category.repository;

import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("카테고리 저장 테스트")
    void saveTest() {
        // given
        Event event = Event.builder().build();
        Event savedEvent = eventRepository.save(event);

        Categories categories = Categories.CLASSIC;
        Category category = Category.builder().categories(categories).event(savedEvent).build();

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(savedCategory).isEqualTo(category);
        assertThat(savedCategory.getId()).isEqualTo(category.getId());
        assertThat(savedCategory.getCategories()).isEqualTo(category.getCategories());
        assertThat(savedCategory.getCategories().name()).isEqualTo(category.getCategories().name());
        assertThat(savedCategory.getCategories().getDescription()).isEqualTo(category.getCategories().getDescription());
        assertThat(savedCategory.getEvent()).isEqualTo(category.getEvent());
    }
}