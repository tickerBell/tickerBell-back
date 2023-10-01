package com.tickerBell.domain.category.service;
import com.tickerBell.domain.category.dtos.CategoryResponse;
import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.event.dtos.EventListResponse;

import java.util.List;


public interface CategoryService {

    Long saveCategory(Long eventId, Categories categories);

    List<CategoryResponse> getAllCategory();
//    List<CategoryResponse> getCategoryListByEvent(Long eventId);
}
