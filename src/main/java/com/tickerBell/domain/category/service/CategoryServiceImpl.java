package com.tickerBell.domain.category.service;

import com.tickerBell.domain.category.dtos.CategoryResponse;
import com.tickerBell.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategory() {
        List<CategoryResponse> categoryResponseList = categoryRepository.findAll().stream()
                .map(category -> CategoryResponse.from(category))
                .collect(Collectors.toList());
        log.info("카테고리 전체 조회");
        return categoryResponseList;
    }

    @Override
    public List<CategoryResponse> getCategoryListByEvent(Long eventId) {
        List<CategoryResponse> categoryResponseList = categoryRepository.findByEventId(eventId).stream()
                .map(category -> CategoryResponse.from(category))
                .collect(Collectors.toList());
        log.info("eventId 에 해당하는 category 조회");
        return categoryResponseList;
    }
}
