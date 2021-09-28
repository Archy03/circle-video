package com.circle.video.repository;

import com.circle.video.model.VideoComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCommentRepository extends JpaRepository<VideoComment, Long> {

  /*@Query("SELECT c from Comment c WHERE c.video.id = ?1")*/
  List<VideoComment> findAllByVideoIdOrderByCreatedAtDesc(Long id);

  List<VideoComment> findAllByVideoIdOrderByCreatedAtAsc(Long id);

  VideoComment findOneById(Long id);

  Integer countVideoCommentByVideoId(Long id);

  void deleteByUserId(Long userId);
}
