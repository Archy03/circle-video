package com.circle.video.controller;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class VideoRestController {

  private final VideoService videoService;

  @PostMapping(value = "/videos/watch/rate/{videoId}/like")
  public String like(@PathVariable("videoId") Long videoId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long loggedUserId = loggedUser.getId();
    try {
      return videoService.vote(videoId, true, loggedUserId) ? "ok" : "change";
    } catch (EntityNotFoundException e) {
      return "id_error";
    }
  }

  @PostMapping(value = "/videos/watch/rate/{videoId}/dislike")
  public String dislike(@PathVariable("videoId") Long videoId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long loggedUserId = loggedUser.getId();
    try {
      return videoService.vote(videoId, false, loggedUserId) ? "ok" : "change";
    } catch (EntityNotFoundException e) {
      return "id_error";
    }
  }
}
