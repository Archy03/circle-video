package com.circle.video.service;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.PictureComment;
import com.circle.video.repository.PictureCommentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class PictureCommentService {

  private final PictureCommentRepository pictureCommentRepository;

  public PictureComment getOneComment(Long id) {
    if (pictureCommentRepository.findOneById(id) == null)
      throw new EntityNotFoundException("Could not found this Comment!");
    return pictureCommentRepository.findOneById(id);
  }

  public void save(PictureComment comment) {
    pictureCommentRepository.save(comment);
  }

  public List<PictureComment> listAllComments(Long id, String direction) {
    if (pictureCommentRepository.findAllByPictureIdOrderByCreatedAtDesc(id) == null)
      throw new EntityNotFoundException("Could not found this Picture!");
    if(direction == null) {
      return pictureCommentRepository.findAllByPictureIdOrderByCreatedAtDesc(id);
    } if(direction.equals("oldest")) {
      return pictureCommentRepository.findAllByPictureIdOrderByCreatedAtAsc(id);
    } else {
      return pictureCommentRepository.findAllByPictureIdOrderByCreatedAtDesc(id);
    }
  }

  public Integer countCommentsByPicture(Long id) {
    return pictureCommentRepository.countVideoCommentByPictureId(id);
  }

  public void delete(Long id) {
    pictureCommentRepository.deleteById(id);
  }

  @Transactional
  public void deleteAllUserComments(Long userId) {
    pictureCommentRepository.deleteByUserId(userId);
  }
}
