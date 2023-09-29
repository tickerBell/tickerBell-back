package com.tickerBell.domain.category.service;

import com.tickerBell.domain.category.entity.Categories;

public interface CategoryService {

    Long saveCategory(Long eventId, Categories categories);
}
