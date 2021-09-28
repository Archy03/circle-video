package com.circle.video.repository;

import com.circle.video.model.PictureRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PictureRatingRepository extends JpaRepository<PictureRating, Long> {

  @Query("SELECT pr FROM PictureRating pr WHERE pr.picture.id = :pictureId AND pr.user.id = :userId")
  PictureRating findUserRating(Long pictureId, Long userId);

  void deleteByUserId(Long userId);
}
