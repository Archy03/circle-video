package com.circle.video.repository;

import com.circle.video.model.Presentation;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PresentationRepository extends PagingAndSortingRepository<Presentation, Long> {

  Presentation findOneById(Long id);

  Long countById(Long id);

  List<Presentation> findAllByOrderByCreatedAtDesc();

  @Query("select p from Presentation p where p.title like %?1% or p.category.categoryName like %?1% or concat(p.user.firstName, ' ', p.user.lastName) like %?1% or concat(p.user.lastName, ' ', p.user.firstName) like %?1%")
  List<Presentation> findByPresentationSort(String keyword, Sort sort);

  @Query("select p from Presentation p where p.title like %:keyword% or p.createdAt like %:keyword% or p.user.userName like %:keyword% or p.category.categoryName like %:keyword% or concat(p.user.firstName, ' ', p.user.lastName) like %:keyword% or concat(p.user.lastName, ' ', p.user.firstName) like %:keyword%")
  Page<Presentation> findAllPageable(String keyword, Pageable pageable);

  Integer countPresentationByUserId(Long userId);

  void deleteByUserId(Long userId);

  List<Presentation> findAllByUserId(Long userId);

  List<Presentation> findAllByUserIdOrderByCreatedAtAsc(Long userId);

  List<Presentation> findAllByUserIdOrderByCreatedAtDesc(Long userId);

  List<Presentation> findAllByUserIdOrderByTitleAsc(Long userId);

  List<Presentation> findAllByUserIdOrderByCategoryAsc(Long userId);
}
