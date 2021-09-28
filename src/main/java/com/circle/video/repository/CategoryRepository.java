package com.circle.video.repository;

import com.circle.video.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findAllByOrderByCategoryNameAsc();
}
