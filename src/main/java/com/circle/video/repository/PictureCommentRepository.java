package com.circle.video.repository;

import com.circle.video.model.PictureComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureCommentRepository extends JpaRepository<PictureComment, Long> {

  List<PictureComment> findAllByPictureIdOrderByCreatedAtDesc(Long id);

  List<PictureComment> findAllByPictureIdOrderByCreatedAtAsc(Long id);

  PictureComment findOneById(Long id);

  Integer countVideoCommentByPictureId(Long id);

  void deleteByUserId(Long userId);
}
