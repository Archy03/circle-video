package com.circle.video.service;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.Element;
import com.circle.video.model.Presentation;
import com.circle.video.model.PresentationRating;
import com.circle.video.model.User;
import com.circle.video.repository.PresentationRatingRepository;
import com.circle.video.repository.PresentationRepository;
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
public class PresentationService {

  private final UserRepository userRepository;
  private final PresentationRepository presentationRepository;
  private final PresentationRatingRepository presentationRatingRepository;

  public List<Presentation> listAllPresentations() {
    return presentationRepository.findAllByOrderByCreatedAtDesc();
  }

  public List<Presentation> listAllPresentationsByUser(Long userId) {
    return presentationRepository.findAllByUserId(userId);
  }

  public Page<Presentation> listAllPresentationsPage(int pageNum, String sortField, String sortDir, String keyword) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(pageNum - 1, Element.ITEMS_PER_PAGE.getValue(), sort);
    if (keyword != null) {
      return presentationRepository.findAllPageable(keyword, pageable);
    }
    return presentationRepository.findAll(pageable);
  }

  public List<Presentation> listAllMyPresentations(Long userId, String direction) {
    if (direction == null) {
      return presentationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    } if (direction.equals("oldest")) {
      return presentationRepository.findAllByUserIdOrderByCreatedAtAsc(userId);
    }
    if (direction.equals("title")) {
      return presentationRepository.findAllByUserIdOrderByTitleAsc(userId);
    }
    if (direction.equals("category")) {
      return presentationRepository.findAllByUserIdOrderByCategoryAsc(userId);
    }
    return presentationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
  }

  public Presentation getOnePresentation(Long id) throws EntityNotFoundException {
    if (presentationRepository.findOneById(id) == null)
      throw new EntityNotFoundException("Could not found this Presentation!");
    return presentationRepository.findOneById(id);
  }

  public boolean vote(Long presentationId, boolean like, Long loggedId) throws EntityNotFoundException {
    User loggedUser = userRepository.findOneById(loggedId);
    Presentation presentation = getOnePresentation(presentationId);
    PresentationRating presentationRating = presentationRatingRepository.findUserRating(presentationId, loggedId);
    if (presentationRating != null) {
      return false;
    }
    presentationRating = new PresentationRating();
    presentationRating.setUser(loggedUser);
    if (like) {
      presentationRating.setLikeValue(Element.VOTE_VALUE.getValue());
    } else {
      presentationRating.setDislikeValue(Element.VOTE_VALUE.getValue());
    }
    presentationRating.setPresentation(presentation);
    presentationRatingRepository.saveAndFlush(presentationRating);
    return true;
  }

  public Integer countPresentationByUser(Long id) {
    return presentationRepository.countPresentationByUserId(id);
  }

  public Presentation save(Presentation presentation) {
    return presentationRepository.save(presentation);
  }

  public void delete(Long presentationId) throws EntityNotFoundException {
    Long countById = presentationRepository.countById(presentationId);
    if (countById == null || countById == 0) {
      throw new EntityNotFoundException("Could not found any Presentation with this ID");
    }
    presentationRepository.deleteById(presentationId);
  }

  @Transactional
  public void deleteAllUserPresentations(Long userId) {
    presentationRepository.deleteByUserId(userId);
  }
}
