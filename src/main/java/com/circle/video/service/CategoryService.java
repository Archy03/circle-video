package com.circle.video.service;

import com.circle.video.model.Category;
import com.circle.video.repository.CategoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public List<Category> listAllCategories() {
    return categoryRepository.findAllByOrderByCategoryNameAsc();
  }
}
