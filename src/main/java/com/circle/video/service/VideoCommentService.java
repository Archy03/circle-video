package com.circle.video.service;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.VideoComment;
import com.circle.video.repository.VideoCommentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class VideoCommentService {

  private final VideoCommentRepository videoCommentRepository;

  public VideoComment getOneComment(Long id) {
    if (videoCommentRepository.findOneById(id) == null)
      throw new EntityNotFoundException("Could not found this Comment!");
    return videoCommentRepository.findOneById(id);
  }

  public void save(VideoComment comment) {
    videoCommentRepository.save(comment);
  }

  public List<VideoComment> listAllComments(Long id, String direction) {
    if (videoCommentRepository.findAllByVideoIdOrderByCreatedAtDesc(id) == null)
      throw new EntityNotFoundException("Could not found this Video!");
    if(direction == null) {
      return videoCommentRepository.findAllByVideoIdOrderByCreatedAtDesc(id);
    } if(direction.equals("oldest")) {
      return videoCommentRepository.findAllByVideoIdOrderByCreatedAtAsc(id);
    } else {
      return videoCommentRepository.findAllByVideoIdOrderByCreatedAtDesc(id);
    }
  }

  public Integer countCommentsByVideo(Long id) {
    return videoCommentRepository.countVideoCommentByVideoId(id);
  }

  public void delete(Long id) {
    videoCommentRepository.deleteById(id);
  }

  @Transactional
  public void deleteAllUserComments(Long userId) {
    videoCommentRepository.deleteByUserId(userId);
  }
}
