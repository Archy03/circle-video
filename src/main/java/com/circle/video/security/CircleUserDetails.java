package com.circle.video.security;

import com.circle.video.model.Role;
import com.circle.video.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class CircleUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Role role = user.getRole();
    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
    authorityList.add(new SimpleGrantedAuthority(role.getRoleName()));
    return authorityList;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUserName();
  }

  public String getRoleName() {
    return user.getRole().getRoleName();
  }

  public Long getId() {
    return user.getId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }

  public String getFullName() {
    return this.user.getFirstName() + " " + this.user.getLastName();
  }

  public String getProfileImagePath() {
    if(this.user.getId() == null || this.user.getProfileImage() == null) return "/images/profile-icon.png";

    return "/media/user-profile-image/" + this.user.getId() + "/" +this.user.getProfileImage();
  }

  public void setProfileImage(String profileImage) {
    this.user.setProfileImage(profileImage);
  }

}
