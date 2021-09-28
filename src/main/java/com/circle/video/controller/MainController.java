package com.circle.video.controller;

import com.circle.video.model.Role;
import com.circle.video.model.User;
import com.circle.video.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class MainController {

  @GetMapping("/login")
  public String viewLoginPage() {
    return "login";
  }

  @GetMapping("/home")
  public String homePage(){
    return "index";
  }

}
