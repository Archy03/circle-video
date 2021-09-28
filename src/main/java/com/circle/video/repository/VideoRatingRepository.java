package com.circle.video.repository;

import com.circle.video.model.VideoRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoRatingRepository extends JpaRepository<VideoRating, Long> {

  @Query("SELECT vr FROM VideoRating vr WHERE vr.video.id = :videoId AND vr.user.id = :userId")
  VideoRating findUserRating(@Param("videoId") Long videoId, @Param("userId") Long userId);

  void deleteByUserId(Long userId);

}
