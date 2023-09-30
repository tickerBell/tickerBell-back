package com.tickerBell.domain.category.service;

import com.tickerBell.domain.category.dtos.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategory();
    List<CategoryResponse> getCategoryListByEvent(Long eventId);
}
