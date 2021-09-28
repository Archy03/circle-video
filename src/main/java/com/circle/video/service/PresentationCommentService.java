package com.circle.video.service;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.PresentationComment;
import com.circle.video.repository.PresentationCommentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class PresentationCommentService {

  private final PresentationCommentRepository presentationCommentRepository;

  public PresentationComment getOneComment(Long id) {
    if (presentationCommentRepository.findOneById(id) == null)
      throw new EntityNotFoundException("Could not found this Comment!");
    return presentationCommentRepository.findOneById(id);
  }

  public void save(PresentationComment comment) {
    presentationCommentRepository.save(comment);
  }

  public List<PresentationComment> listAllComments(Long id, String direction) {
    if (presentationCommentRepository.findAllByPresentationIdOrderByCreatedAtDesc(id) == null)
      throw new EntityNotFoundException("Could not found this Presentation!");
    if(direction == null) {
      return presentationCommentRepository.findAllByPresentationIdOrderByCreatedAtDesc(id);
    } if(direction.equals("oldest")) {
      return presentationCommentRepository.findAllByPresentationIdOrderByCreatedAtAsc(id);
    } else {
      return presentationCommentRepository.findAllByPresentationIdOrderByCreatedAtDesc(id);
    }
  }

  public Integer countCommentsByPresentation(Long id) {
    return presentationCommentRepository.countVideoCommentByPresentationId(id);
  }

  public void delete(Long id) {
    presentationCommentRepository.deleteById(id);
  }

  @Transactional
  public void deleteAllUserComments(Long userId) {
    presentationCommentRepository.deleteByUserId(userId);
  }
}
