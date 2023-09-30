package com.tickerBell.domain.category.repository;

import com.tickerBell.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.event.id = :eventId")
    List<Category> findByEventId(@Param("eventId") Long eventId);
}
