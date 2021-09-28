package com.circle.video.security;

import com.circle.video.model.User;
import com.circle.video.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CircleUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    User user = userRepository.findByUserName(userName);
    if (user != null) {
      return new CircleUserDetails(user);
    }
    throw new UsernameNotFoundException("Could not find user with user name: " + userName);
  }


}
