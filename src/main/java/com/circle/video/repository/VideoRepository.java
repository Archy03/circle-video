package com.circle.video.repository;

import com.circle.video.model.Video;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends PagingAndSortingRepository<Video, Long> {

  Video findOneById(Long id);

  Long countById(Long id);

  List<Video> findAllByOrderByCreatedAtDesc();

  @Query("select v from Video v where v.title like %?1% or v.category.categoryName like %?1% or concat(v.user.firstName, ' ', v.user.lastName) like %?1% or concat(v.user.lastName, ' ', v.user.firstName) like %?1%")
  List<Video> findByVideoSort(Sort sort, String videoKeyword);

  @Query("select v from Video v where v.title like %:keyword% or v.createdAt like %:keyword% or v.user.userName like %:keyword% or v.category.categoryName like %:keyword% or concat(v.user.firstName, ' ', v.user.lastName) like %:keyword% or concat(v.user.lastName, ' ', v.user.firstName) like %:keyword%")
  Page<Video> findAllPageable(String keyword, Pageable pageable);


  Integer countVideoByUserId(Long userId);

  void deleteByUserId(Long userId);

  List<Video> findAllByUserId(Long userId);

  List<Video> findAllByUserIdOrderByCreatedAtAsc(Long userId);

  List<Video> findAllByUserIdOrderByCreatedAtDesc(Long userId);

  List<Video> findAllByUserIdOrderByTitleAsc(Long userId);

  List<Video> findAllByUserIdOrderByCategoryAsc(Long userId);
}
