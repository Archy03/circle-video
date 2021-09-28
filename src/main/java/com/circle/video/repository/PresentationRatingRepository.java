package com.circle.video.repository;

import com.circle.video.model.PresentationRating;
import com.circle.video.model.VideoRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PresentationRatingRepository extends JpaRepository<PresentationRating, Long> {

  @Query("SELECT pr FROM PresentationRating pr WHERE pr.presentation.id = :presentationId AND pr.user.id = :userId")
  PresentationRating findUserRating(Long presentationId, Long userId);

  void deleteByUserId(Long userId);
}
