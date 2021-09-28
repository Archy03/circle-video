package com.circle.video.service;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.Category;
import com.circle.video.model.Element;
import com.circle.video.model.VideoRating;
import com.circle.video.model.User;
import com.circle.video.model.Video;
import com.circle.video.repository.CategoryRepository;
import com.circle.video.repository.VideoRatingRepository;
import com.circle.video.repository.UserRepository;
import com.circle.video.repository.VideoRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class VideoService {

  private final VideoRepository videoRepository;
  private final VideoRatingRepository videoRatingRepository;
  private final UserRepository userRepository;


  public List<Video> listAllVideos() {
    return videoRepository.findAllByOrderByCreatedAtDesc();
  }

  public List<Video> listAllVideosByUser(Long userId) {
    return videoRepository.findAllByUserId(userId);
  }

  public Page<Video> listAllVideosPage(int pageNum, String sortField, String sortDir, String videoKeyword) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(pageNum - 1, Element.ITEMS_PER_PAGE.getValue(), sort);
    if (videoKeyword != null) {
      return videoRepository.findAllPageable(videoKeyword, pageable);
    }
    return videoRepository.findAll(pageable);
  }


  public List<Video> listAllMyVideos(Long userId, String direction) {
    if (direction == null) {
      return videoRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    } if (direction.equals("oldest")) {
      return videoRepository.findAllByUserIdOrderByCreatedAtAsc(userId);
    }
    if (direction.equals("title")) {
      return videoRepository.findAllByUserIdOrderByTitleAsc(userId);
    }
    if (direction.equals("category")) {
      return videoRepository.findAllByUserIdOrderByCategoryAsc(userId);
    }
    return videoRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
  }

  public Video getOneVideo(Long id) throws EntityNotFoundException {
    if (videoRepository.findOneById(id) == null)
      throw new EntityNotFoundException("Could not found this Video!");
    return videoRepository.findOneById(id);
  }

  public boolean vote(Long videoId, boolean like, Long loggedId) throws EntityNotFoundException {
    User loggedUser = userRepository.findOneById(loggedId);
    Video video = getOneVideo(videoId);
    VideoRating videoRating = videoRatingRepository.findUserRating(videoId, loggedId);
    if (videoRating != null) {
      return false;
    }
    videoRating = new VideoRating();
    videoRating.setUser(loggedUser);
    if (like) {
      videoRating.setLikeValue(Element.VOTE_VALUE.getValue());
    } else {
      videoRating.setDislikeValue(Element.VOTE_VALUE.getValue());
    }
    videoRating.setVideo(video);
    videoRatingRepository.saveAndFlush(videoRating);
    return true;
  }

  public Integer countVideoByUser(Long id) {
    return videoRepository.countVideoByUserId(id);
  }

  public Video save(Video video) {
    return videoRepository.save(video);
  }

  public void delete(Long videoId) throws EntityNotFoundException {
    Long countById = videoRepository.countById(videoId);
    if (countById == null || countById == 0) {
      throw new EntityNotFoundException("Could not found any Video with this id");
    }
    videoRepository.deleteById(videoId);
  }

  @Transactional
  public void deleteAllUserVideos(Long userId) {
    videoRepository.deleteByUserId(userId);
  }


}
