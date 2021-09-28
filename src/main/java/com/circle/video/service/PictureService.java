package com.circle.video.service;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.Element;
import com.circle.video.model.Picture;
import com.circle.video.model.PictureRating;
import com.circle.video.model.User;
import com.circle.video.repository.PictureRatingRepository;
import com.circle.video.repository.PictureRepository;
import com.circle.video.repository.UserRepository;
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
public class PictureService {

  private final UserRepository userRepository;
  private final PictureRepository pictureRepository;
  private final PictureRatingRepository pictureRatingRepository;

  public List<Picture> listAllPictures() {
    return pictureRepository.findAllByOrderByCreatedAtDesc();
  }

  public List<Picture> listAllPicturesByUser(Long userId) {
    return pictureRepository.findAllByUserId(userId);
  }

  public Page<Picture> listAllPicturesPage(int pageNum, String sortField, String sortDir, String keyword) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(pageNum - 1, Element.ITEMS_PER_PAGE.getValue(), sort);
    if (keyword != null) {
      return pictureRepository.findAllPageable(keyword, pageable);
    }
    return pictureRepository.findAll(pageable);
  }

  public List<Picture> listAllMyPictures(Long userId, String direction) {
    if (direction == null) {
      return pictureRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    } if (direction.equals("oldest")) {
      return pictureRepository.findAllByUserIdOrderByCreatedAtAsc(userId);
    }
    if (direction.equals("title")) {
      return pictureRepository.findAllByUserIdOrderByTitleAsc(userId);
    }
    if (direction.equals("category")) {
      return pictureRepository.findAllByUserIdOrderByCategoryAsc(userId);
    }
    return pictureRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
  }

  public Picture getOnePicture(Long id) throws EntityNotFoundException {
    if (pictureRepository.findOneById(id) == null)
      throw new EntityNotFoundException("Could not found this Picture!");
    return pictureRepository.findOneById(id);
  }

  public boolean vote(Long pictureId, boolean like, Long loggedId) throws EntityNotFoundException {
    User loggedUser = userRepository.findOneById(loggedId);
    Picture picture = getOnePicture(pictureId);
    PictureRating pictureRating = pictureRatingRepository.findUserRating(pictureId, loggedId);
    if (pictureRating != null) {
      return false;
    }
    pictureRating = new PictureRating();
    pictureRating.setUser(loggedUser);
    if (like) {
      pictureRating.setLikeValue(Element.VOTE_VALUE.getValue());
    } else {
      pictureRating.setDislikeValue(Element.VOTE_VALUE.getValue());
    }
    pictureRating.setPicture(picture);
    pictureRatingRepository.saveAndFlush(pictureRating);
    return true;
  }

  public Integer countPictureByUser(Long id) {
    return pictureRepository.countPictureByUserId(id);
  }

  public Picture save(Picture picture) {
    return pictureRepository.save(picture);
  }

  public void delete(Long pictureId) throws EntityNotFoundException {
    Long countById = pictureRepository.countById(pictureId);
    if (countById == null || countById == 0) {
      throw new EntityNotFoundException("Could not found any Picture with this ID");
    }
    pictureRepository.deleteById(pictureId);
  }

  @Transactional
  public void deleteAllUserPictures(Long userId) {
    pictureRepository.deleteByUserId(userId);
  }
}
