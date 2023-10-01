package com.tickerBell.domain.category.service;

import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.category.repository.CategoryRepository;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import com.tickerBell.domain.category.dtos.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Long saveCategory(Long eventId, Categories categories) {
//        Event findEvent = eventRepository.findById(eventId).orElseThrow(
//                () -> new CustomException(ErrorCode.EVENT_NOT_FOUND)
//        );

        Category category = Category.builder()
                .categories(categories)
//                .event(findEvent)
                .build();
        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getId();
    }

    @Override
    public List<CategoryResponse> getAllCategory() {
        List<CategoryResponse> categoryResponseList = categoryRepository.findAll().stream()
                .map(category -> CategoryResponse.from(category))
                .collect(Collectors.toList());
        log.info("카테고리 전체 조회");
        return categoryResponseList;
    }



}
