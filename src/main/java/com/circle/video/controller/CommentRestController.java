package com.circle.video.controller;

import com.circle.video.exception.ForbiddenException;
import com.circle.video.model.Picture;
import com.circle.video.model.PictureComment;
import com.circle.video.model.Presentation;
import com.circle.video.model.PresentationComment;
import com.circle.video.model.VideoComment;
import com.circle.video.model.User;
import com.circle.video.model.Video;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.PictureCommentService;
import com.circle.video.service.PictureService;
import com.circle.video.service.PresentationCommentService;
import com.circle.video.service.PresentationService;
import com.circle.video.service.VideoCommentService;
import com.circle.video.service.UserService;
import com.circle.video.service.VideoService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentRestController {

  private final UserService userService;
  private final VideoService videoService;
  private final VideoCommentService videoCommentService;
  private final PresentationService presentationService;
  private final PresentationCommentService presentationCommentService;
  private final PictureService pictureService;
  private final PictureCommentService pictureCommentService;

  @PostMapping("videoComment/save/{videoId}")
  public String saveComment(@AuthenticationPrincipal CircleUserDetails loggedUser,
      VideoComment videoComment,
      @PathVariable Long videoId) {

    User user = userService.getUserByUserName(loggedUser.getUsername());
    LocalDateTime now = LocalDateTime.now();
    Video video = videoService.getOneVideo(videoId);
    videoComment.setCreatedAt(now);
    videoComment.setUser(user);
    videoComment.setVideo(video);
    videoCommentService.save(videoComment);
    return "ok";
  }

  @GetMapping("videoComment/delete/{commentId}")
  public String deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long userId = loggedUser.getId();
    String userRoleName = loggedUser.getRoleName();
    VideoComment comment = videoCommentService.getOneComment(commentId);
    Long commentUserId = comment.getUser().getId();
    if (userId.equals(commentUserId) || userRoleName.equals("Assistant") || userRoleName.equals("Admin")) {
      videoCommentService.delete(commentId);
      return "ok";
    } else {
      throw new ForbiddenException("You don't have permission to delete this Comment!");
    }
  }

  @PostMapping("presentationComment/save/{presentationId}")
  public String savePresentationComment(@AuthenticationPrincipal CircleUserDetails loggedUser,
      PresentationComment presentationComment,
      @PathVariable Long presentationId) {

    User user = userService.getUserByUserName(loggedUser.getUsername());
    LocalDateTime now = LocalDateTime.now();
    Presentation presentation = presentationService.getOnePresentation(presentationId);
    presentationComment.setCreatedAt(now);
    presentationComment.setUser(user);
    presentationComment.setPresentation(presentation);
    presentationCommentService.save(presentationComment);
    return "ok";
  }

  @GetMapping("presentationComment/delete/{commentId}")
  public String deletePresentationComment(@PathVariable Long commentId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long userId = loggedUser.getId();
    String userRoleName = loggedUser.getRoleName();
    PresentationComment comment = presentationCommentService.getOneComment(commentId);
    Long commentUserId = comment.getUser().getId();
    if (userId.equals(commentUserId) || userRoleName.equals("Assistant") || userRoleName.equals("Admin")) {
      presentationCommentService.delete(commentId);
      return "ok";
    } else {
      throw new ForbiddenException("You don't have permission to delete this Comment!");
    }
  }

  @PostMapping("pictureComment/save/{pictureId}")
  public String savePictureComment(@AuthenticationPrincipal CircleUserDetails loggedUser,
      PictureComment pictureComment,
      @PathVariable Long pictureId) {

    User user = userService.getUserByUserName(loggedUser.getUsername());
    LocalDateTime now = LocalDateTime.now();
    Picture picture = pictureService.getOnePicture(pictureId);
    pictureComment.setCreatedAt(now);
    pictureComment.setUser(user);
    pictureComment.setPicture(picture);
    pictureCommentService.save(pictureComment);
    return "ok";
  }

  @GetMapping("pictureComment/delete/{commentId}")
  public String deletePictureComment(@PathVariable Long commentId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long userId = loggedUser.getId();
    String userRoleName = loggedUser.getRoleName();
    PictureComment comment = pictureCommentService.getOneComment(commentId);
    Long commentUserId = comment.getUser().getId();
    if (userId.equals(commentUserId) || userRoleName.equals("Assistant") || userRoleName.equals("Admin")) {
      pictureCommentService.delete(commentId);
      return "ok";
    } else {
      throw new ForbiddenException("You don't have permission to delete this Comment!");
    }
  }
}
