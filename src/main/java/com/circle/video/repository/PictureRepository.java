package com.circle.video.repository;

import com.circle.video.model.Picture;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PictureRepository extends PagingAndSortingRepository<Picture, Long> {

  Picture findOneById(Long id);

  Long countById(Long id);

  List<Picture> findAllByOrderByCreatedAtDesc();

  @Query("select p from Picture p where p.title like %?1% or p.category.categoryName like %?1% or concat(p.user.firstName, ' ', p.user.lastName) like %?1% or concat(p.user.lastName, ' ', p.user.firstName) like %?1%")
  List<Picture> findByPictureSort(Sort sort, String keyword);

  @Query("select p from Picture p where p.title like %:keyword% or p.createdAt like %:keyword% or p.user.userName like %:keyword% or p.category.categoryName like %:keyword% or concat(p.user.firstName, ' ', p.user.lastName) like %:keyword% or concat(p.user.lastName, ' ', p.user.firstName) like %:keyword%")
  Page<Picture> findAllPageable(String keyword, Pageable pageable);

  Integer countPictureByUserId(Long userId);

  void deleteByUserId(Long userId);

  List<Picture> findAllByUserId(Long userId);

  List<Picture> findAllByUserIdOrderByCreatedAtAsc(Long userId);

  List<Picture> findAllByUserIdOrderByCreatedAtDesc(Long userId);

  List<Picture> findAllByUserIdOrderByTitleAsc(Long userId);

  List<Picture> findAllByUserIdOrderByCategoryAsc(Long userId);
}
