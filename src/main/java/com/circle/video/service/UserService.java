package com.circle.video.service;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.Element;
import com.circle.video.model.Role;
import com.circle.video.model.User;
import com.circle.video.repository.RoleRepository;
import com.circle.video.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public User getUserByUserName(String userName) {
    return userRepository.findByUserName(userName);
  }

  public User getUserById(Long id) throws EntityNotFoundException {
    if(userRepository.findOneById(id) == null) {
      throw new EntityNotFoundException("Could not found any User with ID " + id + "!");
    }
    return userRepository.findOneById(id);
  }

  public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(pageNum - 1, Element.ITEMS_PER_PAGE.getValue(), sort);
    if (keyword != null) {
      return userRepository.findAll(keyword, pageable);
    }
    return userRepository.findAll(pageable);
  }

  public List<Role> listRoles() {
    return roleRepository.findAllByOrderByRoleNameDesc();
  }

  public User updateAccount(User userInForm) {
    User userInDB = userRepository.findOneById(userInForm.getId());
    if (userInForm.getProfileImage() != null) {
      userInDB.setProfileImage(userInForm.getProfileImage());
    }
    return userRepository.save(userInDB);
  }

  public void encodePassword(User user) {
    String encodePassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodePassword);
  }

  public boolean isEmailUnique(Long id, String email) {
    User userEmail = userRepository.findByEmail(email);
    if (userEmail == null) {
      return true;
    }
    if (id == null) {
      return false;
    } else {
      return userEmail.getId().equals(id);
    }
  }

  public boolean isUserNameUnique(Long id, String userN) {
    User userName = userRepository.findByUserName(userN);
    if (userName == null) {
      return true;
    }
    if (id == null) {
      return false;
    } else {
      return userName.getId().equals(id);
    }
  }

  public User save(User user) {
    boolean isUpdatingUser = user.getId() != null;
    if (isUpdatingUser) {
      User existingUser = userRepository.findOneById(user.getId());
      if (user.getPassword().isEmpty()) {
        user.setPassword(existingUser.getPassword());
      } else {
        encodePassword(user);
      }
    } else {
      encodePassword(user);
    }
    return userRepository.save(user);
  }

  @Transactional
  public void delete(Long id) throws EntityNotFoundException {
    Long countById = userRepository.countById(id);
    if (countById == null || countById == 0) {
      throw new EntityNotFoundException("Could not found any User with ID " + id + "!");
    }
    userRepository.deleteById(id);
  }
}
