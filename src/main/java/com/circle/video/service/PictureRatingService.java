package com.circle.video.service;

import com.circle.video.repository.PictureRatingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class PictureRatingService {

  private final PictureRatingRepository pictureRatingRepository;

  @Transactional
  public void deleteAllUserRatings(Long userId) {
    pictureRatingRepository.deleteByUserId(userId);
  }
}
