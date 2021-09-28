package com.circle.video.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "Role is required")
  @Column(unique = true)
  private String categoryName;

  public Category(String categoryName) {
    this.categoryName = categoryName;
  }

  public Category(Long id) {
    this.id = id;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return id.equals(category.id);
  }

  @Override public int hashCode() {
    return Objects.hash(id);
  }

  @Override public String toString() {
    return this.categoryName;
  }
}
