package com.circle.video.service;

import com.circle.video.repository.VideoRatingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class VideoRatingService {

  private final VideoRatingRepository videoRatingRepository;

  @Transactional
  public void deleteAllUserRatings(Long userId) {
    videoRatingRepository.deleteByUserId(userId);
  }
}
