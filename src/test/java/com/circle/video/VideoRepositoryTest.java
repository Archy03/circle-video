package com.circle.video;

import com.circle.video.model.Category;
import com.circle.video.model.Role;
import com.circle.video.model.User;
import com.circle.video.model.Video;
import com.circle.video.repository.UserRepository;
import com.circle.video.repository.VideoRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class VideoRepositoryTest {

  @Autowired
  private VideoRepository videoRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Test
  public void testUploadVideo() {
    LocalDateTime now = LocalDateTime.now();
    Category category1 = entityManager.find(Category.class, 1L);
    User user1 = entityManager.find(User.class, 69L);
    Video video1 = new Video("Video cim2", "Video leiras2", "Video utvonal2", now, category1, user1);
    Video savedVideo1 = videoRepository.save(video1);
    Assertions.assertThat(savedVideo1.getId()).isGreaterThan(0);
  }
}
