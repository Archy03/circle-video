package com.circle.video.service;

import com.circle.video.repository.PresentationRatingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class PresentationRatingService {

  private final PresentationRatingRepository presentationRatingRepository;

  @Transactional
  public void deleteAllUserRatings(Long userId) {
    presentationRatingRepository.deleteByUserId(userId);
  }
}
