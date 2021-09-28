package com.circle.video.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "First name is required")
  private String firstName;
  @NotNull(message = "Last name is required")
  private String lastName;
  @NotNull(message = "Username is required")
  @Column(unique = true)
  private String userName;
  @NotNull(message = "Password is required")
  private String password;
  @Email
  @NotNull(message = "Email is required")
  @Column(unique = true)
  private String email;
  @ManyToOne
  private Role role;
  private String profileImage;
  private boolean enabled;

  public User(String firstName, String lastName, String userName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override public int hashCode() {
    return Objects.hash(id);
  }

  @Transient
  public String getProfileImagePath() {
    if(id == null || profileImage == null) {
      return "/images/profile-icon.png";
    }
    return "/media/user-profile-image/" + this.id + "/" + this.profileImage;
  }

  @Transient
  public String getFullUserName() {
    return getFirstName() + " " + getLastName();
  }

}
