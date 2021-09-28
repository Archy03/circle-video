package com.circle.video.controller;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.PictureService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PictureRestController {

  private final PictureService pictureService;

  @PostMapping(value = "/pictures/watch/rate/{pictureId}/like")
  public String like(@PathVariable("pictureId") Long pictureId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long loggedUserId = loggedUser.getId();
    try {
      return pictureService.vote(pictureId, true, loggedUserId) ? "ok" : "change";
    } catch (EntityNotFoundException e) {
      return "id_error";
    }
  }

  @PostMapping(value = "/pictures/watch/rate/{pictureId}/dislike")
  public String dislike(@PathVariable("pictureId") Long pictureId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long loggedUserId = loggedUser.getId();
    try {
      return pictureService.vote(pictureId, false, loggedUserId) ? "ok" : "change";
    } catch (EntityNotFoundException e) {
      return "id_error";
    }
  }
}
