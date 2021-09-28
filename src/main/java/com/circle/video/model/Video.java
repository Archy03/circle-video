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
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  @Column(columnDefinition = "TEXT")
  private String description;
  private String videoFile;
  private String videoThumbnail;
  @CreationTimestamp
  private LocalDateTime createdAt;

  @ManyToOne
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VideoComment> comments = new ArrayList<>();

  @OneToMany( mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VideoRating> videoRatings = new ArrayList<>();

  public Video(String title, String description, String videoThumbnail, LocalDateTime createdAt, Category category, User user) {
    this.title = title;
    this.description = description;
    this.videoThumbnail = videoThumbnail;
    this.createdAt = createdAt;
    this.category = category;
    this.user = user;
  }
  

  @Transient
  public String getVideoThumbnailPath() {
    if(id == null || videoThumbnail == null) {
      return "/images/default-video-thumbnail.jpg";
    }
    return "/media/user-videos/" + user.getId() + "/" + this.id + "/" + this.videoThumbnail;
  }

  @Transient
  public String getVideoPath() {
    return "/media/user-videos/" + user.getId() + "/" + this.id + "/" + this.videoFile;
  }

  @Transient
  public int getLikeRatingSum() {
    return videoRatings.stream().mapToInt(VideoRating::getLikeValue).sum();
  }

  @Transient
  public int getDislikeRatingSum() {
    return videoRatings.stream().mapToInt(VideoRating::getDislikeValue).sum();
  }

  @Transient
  public String calculatedPercent() {
    return Common.likeDislikePercent(getLikeRatingSum(), getDislikeRatingSum());
  }

  @Transient
  public short getUserLikeVoteValue(Long userId) {
    if (userId == null)
      return 0;
    Optional<VideoRating> rating = videoRatings.stream().filter(r -> r.getUser().getId().equals(userId)).findFirst();
    return rating.isPresent() ? rating.get().getLikeValue() : 0;
  }

  @Transient
  public short getUserDislikeVoteValue(Long userId) {
    if (userId == null)
      return 0;
    Optional<VideoRating> rating = videoRatings.stream().filter(r -> r.getUser().getId().equals(userId)).findFirst();
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
    Video video = (Video) o;
    return Objects.equals(id, video.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
