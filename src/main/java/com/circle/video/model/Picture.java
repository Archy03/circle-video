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
public class Picture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String pictureFile;
  @Column(columnDefinition = "TEXT")
  private String description;
  @CreationTimestamp
  private LocalDateTime createdAt;

  @ManyToOne
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "picture", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PictureComment> pictureComments = new ArrayList<>();

  @OneToMany( mappedBy = "picture", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PictureRating> pictureRatings = new ArrayList<>();

  @Transient
  public String getPicturePath() {
    return "/media/user-pictures/" + user.getId() + "/" + this.id + "/" + this.pictureFile;
  }

  @Transient
  public int getLikeRatingSum() {
    return pictureRatings.stream().mapToInt(PictureRating::getLikeValue).sum();
  }

  @Transient
  public int getDislikeRatingSum() {
    return pictureRatings.stream().mapToInt(PictureRating::getDislikeValue).sum();
  }

  @Transient
  public String calculatedPercent() {
    return Common.likeDislikePercent(getLikeRatingSum(), getDislikeRatingSum());
  }

  @Transient
  public short getUserLikeVoteValue(Long userId) {
    if (userId == null)
      return 0;
    Optional<PictureRating> rating = pictureRatings.stream().filter(r -> r.getUser().getId().equals(userId)).findFirst();
    return rating.isPresent() ? rating.get().getLikeValue() : 0;
  }

  @Transient
  public short getUserDislikeVoteValue(Long userId) {
    if (userId == null)
      return 0;
    Optional<PictureRating> rating = pictureRatings.stream().filter(r -> r.getUser().getId().equals(userId)).findFirst();
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
    Picture picture = (Picture) o;
    return Objects.equals(id, picture.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
