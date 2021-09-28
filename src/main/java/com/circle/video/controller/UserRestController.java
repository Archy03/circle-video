package com.circle.video.controller;

import com.circle.video.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserRestController {

  private final UserService userService;

  @PostMapping("/users/check_email")
  public String checkDuplicateEmail(@Param("id") Long id, @Param("email") String email) {
    return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
  }

  @PostMapping("/users/check_username")
  public String checkDuplicateUserName(@Param("id") Long id, @Param("userName") String userName) {
    return userService.isUserNameUnique(id, userName) ? "OK" : "Duplicated";
  }

}
