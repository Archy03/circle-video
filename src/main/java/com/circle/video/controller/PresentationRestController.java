package com.circle.video.controller;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.PresentationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PresentationRestController {

  private final PresentationService presentationService;

  @PostMapping(value = "/presentations/watch/rate/{presentationId}/like")
  public String like(@PathVariable("presentationId") Long presentationId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long loggedUserId = loggedUser.getId();
    try {
      return presentationService.vote(presentationId, true, loggedUserId) ? "ok" : "change";
    } catch (EntityNotFoundException e) {
      return "id_error";
    }
  }

  @PostMapping(value = "/presentations/watch/rate/{presentationId}/dislike")
  public String dislike(@PathVariable("presentationId") Long presentationId, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long loggedUserId = loggedUser.getId();
    try {
      return presentationService.vote(presentationId, false, loggedUserId) ? "ok" : "change";
    } catch (EntityNotFoundException e) {
      return "id_error";
    }
  }
}
