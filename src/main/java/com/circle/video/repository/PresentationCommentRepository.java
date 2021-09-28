package com.circle.video.repository;

import com.circle.video.model.PresentationComment;
import com.circle.video.model.VideoComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationCommentRepository extends JpaRepository<PresentationComment, Long> {

  List<PresentationComment> findAllByPresentationIdOrderByCreatedAtDesc(Long id);

  List<PresentationComment> findAllByPresentationIdOrderByCreatedAtAsc(Long id);

  PresentationComment findOneById(Long id);

  Integer countVideoCommentByPresentationId(Long id);

  void deleteByUserId(Long userId);
}
