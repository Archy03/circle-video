package com.circle.video;

import com.circle.video.model.Role;
import com.circle.video.model.User;
import com.circle.video.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Test
  public void testCreateAdmin() {
    Role adminRole = entityManager.find(Role.class, (short)1);
    User userAdmin = new User("Csongor", "Buru", "admin", "admin@gmail.com");

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String adminPassword = "admin";
    String encodedPassword = passwordEncoder.encode(adminPassword);
    userAdmin.setPassword(encodedPassword);
    userAdmin.setRole(adminRole);
    userAdmin.setEnabled(true);
    userRepository.save(userAdmin);
  }

  @Test
  public void testCreateUsers() {
    Role userRole = entityManager.find(Role.class, (short)3);
    User user1 = new User("Ferenc", "Kerepes", "feke", "feke@pm.com");
    User user2 = new User("Júlia", "Kis", "juki", "juki@gmail.com");
    User user3 = new User("Márta", "Nagy", "mana", "mana@yahoo.com");
    User user4 = new User("Béla", "Budai", "bebu", "bebu@gmail.com");
    User user5 = new User("Gábor", "Rác", "gara", "gara@yahoo.com");
    User user6 = new User("Béla", "Hosszú", "beho", "beho@gmail.com");
    User user7 = new User("János", "Pataki", "japa", "japa@gmail.com");
    User user8 = new User("Béla", "Nagy", "bena", "bena@yahoo.com");
    User user9 = new User("Mária", "Nagy", "nama", "nama@pm.com");
    User user10 = new User("Róbert", "Pál", "ropa", "ropa@gmail.com");
    User user11 = new User("Krisztián", "Sánta", "krsa", "krsa@gmail.com");
    User user12 = new User("Renáta", "Tóth", "reto", "reto@gmail.com");
    User user13 = new User("Márta", "Horvát", "maho", "maho@pm.com");
    User user14 = new User("Emese", "Csonka", "emcso", "emcso@gmail.com");
    User user15 = new User("Emese", "Molnár", "emmo", "emmo@yahoo.com");
    User user16 = new User("Nándor", "Szabó", "nasza", "nasza@pm.com");
    List<User> users = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12, user13, user14, user15, user16);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    int i = 1;
    for (User u: users) {
      String password = "userpass" + i;
      String encodedPassword = passwordEncoder.encode(password);
      u.setPassword(encodedPassword);
      u.setRole(userRole);
      u.setEnabled(true);
      i++;
    }
    userRepository.saveAll(users);
  }


  @Test
  public void testListAllUsers() {
    Iterable<User> listUsers = userRepository.findAll();
    listUsers.forEach(System.out::println);

  }

  @Test
  public void testGetUserById() {
    User user = userRepository.findById(1L).orElse(null);
    Assertions.assertThat(user).isNotNull();
  }

  @Test
  public void testUpdateUserDetails() {
    User user = userRepository.findById(1L).orElse(null);
    assert user != null;
    user.setEnabled(true);
    userRepository.save(user);
  }

  @Test
  public void testUpdateUserRoles(){
    User user = userRepository.findById(1L).orElse(null);
    Role roleEditor = new Role((short) 1);
    user.setRole(roleEditor);
    userRepository.save(user);
  }

  @Test
  public void testDeleteUserRoles(){
    User user = userRepository.findById(3L).orElse(null);
    user.setRole(null);
    userRepository.save(user);
  }

  @Test
  public void testDeleteUser() {
    userRepository.deleteById(3L);
  }

  @Test
  public void testGetUserByEmail() {
    String email = "123@gmail.com";
    User user = userRepository.findByEmail(email);
    Assertions.assertThat(user).isNotNull();
    //Assertions.assertThat(user).isNull();
  }

  @Test
  public void testCountById() {
    Long id = 10L;
    Long countById = userRepository.countById(id);
    Assertions.assertThat(countById).isNotNull().isEqualTo(1);
  }

  @Test
  public void testListFirstPage() {
    int pageNumber = 0;
    int pageSize = 4;

    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<User> page = userRepository.findAll(pageable);

    List<User> listUsers = page.getContent();
    //listUsers.forEach(user -> System.out.println(user));
    listUsers.forEach(System.out::println);
    Assertions.assertThat(listUsers.size()).isEqualTo(pageSize);
  }

  @Test
  public void testSearchUsers() {
    String keyword = "pál";
    int pageNumber = 0;
    int pageSize = 6;

    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<User> page = userRepository.findAll(keyword, pageable);

    List<User> listUsers = page.getContent();
    //listUsers.forEach(user -> System.out.println(user));
    listUsers.forEach(System.out::println);
    Assertions.assertThat(listUsers.size()).isGreaterThan(0);
  }
}
