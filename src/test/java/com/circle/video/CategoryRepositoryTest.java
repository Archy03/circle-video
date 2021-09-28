package com.circle.video;

import com.circle.video.model.Category;
import com.circle.video.repository.CategoryRepository;
import com.circle.video.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository categoryRepository;


  @Test
  public void testCreateCategoryList() {
    Category category1 = new Category("Animation");
    Category category2 = new Category("Autos & Vehicles");
    Category category3 = new Category("Comedy");
    Category category4 = new Category("Education");
    Category category5 = new Category("Film");
    Category category6 = new Category("Gaming");
    Category category7 = new Category("IT");
    Category category8 = new Category("Music");
    Category category9 = new Category("News & Politics");
    Category category10 = new Category("People");
    Category category11 = new Category("Personal");
    Category category12 = new Category("Pets & Animals");
    Category category13 = new Category("Picture");
    Category category14 = new Category("Presentation");
    Category category15 = new Category("Science & Technology");
    Category category16 = new Category("Sports");
    Category category17 = new Category("Stop Motion");
    Category category18 = new Category("Travel");
    Category category19 = new Category("Video");

    categoryRepository.saveAll(List.of(
        category1,
        category2,
        category3,
        category4,
        category5,
        category6,
        category7,
        category8,
        category9,
        category10,
        category11,
        category12,
        category13,
        category14,
        category15,
        category16,
        category17,
        category18,
        category19));
  }
}
