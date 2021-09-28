package com.circle.video.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class PresentationComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(columnDefinition = "TEXT")
  @NotNull(message = "Comment is required")
  private String text;
  @CreationTimestamp
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
  @ManyToOne(fetch = FetchType.LAZY)
  private Presentation presentation;

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PresentationComment that = (PresentationComment) o;
    return Objects.equals(id, that.id);
  }

  @Override public int hashCode() {
    return Objects.hash(id);
  }
}
