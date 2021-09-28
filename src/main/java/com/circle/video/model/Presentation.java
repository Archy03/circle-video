package com.circle.video.model;

import com.circle.video.util.Common;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Presentation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  @Column(columnDefinition = "TEXT")
  private String description;
  private String presentationFile;
  private String presentationThumbnail;
  @CreationTimestamp
  private LocalDateTime createdAt;

  @ManyToOne
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "presentation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PresentationComment> presentationComments = new ArrayList<>();

  @OneToMany( mappedBy = "presentation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PresentationRating> presentationRatings = new ArrayList<>();

  @Transient
  public String getPresentationThumbnailPath() {
    if(id == null || presentationThumbnail == null) {
      return "/images/default-presentation-thumbnail.jpg";
    }
    return "/media/user-presentations/" + user.getId() + "/" + this.id + "/" + this.presentationThumbnail;
  }

  @Transient
  public String getPresentationPath() {
    return "/media/user-presentations/" + user.getId() + "/" + this.id + "/" + this.presentationFile;
  }

  @Transient
  public int getLikeRatingSum() {
    return presentationRatings.stream().mapToInt(PresentationRating::getLikeValue).sum();
  }

  @Transient
  public int getDislikeRatingSum() {
    return presentationRatings.stream().mapToInt(PresentationRating::getDislikeValue).sum();
  }

  @Transient
  public String calculatedPercent() {
    return Common.likeDislikePercent(getLikeRatingSum(), getDislikeRatingSum());
  }

  @Transient
  public short getUserLikeVoteValue(Long userId) {
    if (userId == null)
      return 0;
    Optional<PresentationRating> rating = presentationRatings.stream().filter(r -> r.getUser().getId().equals(userId)).findFirst();
    return rating.isPresent() ? rating.get().getLikeValue() : 0;
  }

  @Transient
  public short getUserDislikeVoteValue(Long userId) {
    if (userId == null)
      return 0;
    Optional<PresentationRating> rating = presentationRatings.stream().filter(r -> r.getUser().getId().equals(userId)).findFirst();
    return rating.isPresent() ? rating.get().getDislikeValue() : 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Presentation presentation = (Presentation) o;
    return Objects.equals(id, presentation.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
