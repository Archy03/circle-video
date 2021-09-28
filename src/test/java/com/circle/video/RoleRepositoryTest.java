package com.circle.video;

import com.circle.video.model.Role;
import com.circle.video.repository.RoleRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

  @Autowired
  private RoleRepository roleRepository;

  @Test
  public void testCreateSingleRole() {
    Role role = new Role("Admin");
    Role savedRole = roleRepository.save(role);
    Assertions.assertThat(savedRole.getId()).isGreaterThan((short) 0);
  }

  @Test
  public void testCreateMultipleRole() {
    Role role1 = new Role("Admin");
    Role role2 = new Role("Assistant");
    Role role3 = new Role("User");
    roleRepository.saveAll(List.of(role1, role2, role3));
  }
}
