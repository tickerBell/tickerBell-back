package com.tickerBell.domain.image.repository;

import com.tickerBell.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // eventId 에 해당하는 image 조회
    @Query("select i from Image i where i.event.id = :eventId")
    List<Image> findImageByEventId(@Param("eventId") Long eventId);
    @Query("select i from Image i where i.s3Url = :imageUrl")
    Optional<Image> findByImageUrl(@Param("imageUrl") String imageUrl);
}
