package com.circle.video.controller;

import com.circle.video.model.Picture;
import com.circle.video.model.PictureComment;
import com.circle.video.model.Presentation;
import com.circle.video.model.PresentationComment;
import com.circle.video.model.User;
import com.circle.video.model.Video;
import com.circle.video.model.VideoComment;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.PictureCommentService;
import com.circle.video.service.PictureService;
import com.circle.video.service.PresentationCommentService;
import com.circle.video.service.PresentationService;
import com.circle.video.service.UserService;
import com.circle.video.service.VideoCommentService;
import com.circle.video.service.VideoService;
import com.circle.video.util.Common;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/guest")
public class GuestController {

  private final UserService userService;

  private final VideoService videoService;
  private final VideoCommentService videoCommentService;

  private final PresentationService presentationService;
  private final PresentationCommentService presentationCommentService;

  private final PictureService pictureService;
  private final PictureCommentService pictureCommentService;

  @GetMapping("/home")
  public String home(){
    return "/guest/guest_home";
  }

  @GetMapping("/videos")
  public String viewVideos(Model model) {
    return listVideosPage(1, model, null, "createdAt", "desc");
  }

  @GetMapping("/videos/page/{pageNum}")
  public String listVideosPage(@PathVariable int pageNum,
      Model model,
      String keyword,
      String sortField,
      String sortDir) {
    Common.listAllVideosPage(pageNum, model, keyword, sortField, sortDir, videoService);
    return "/guest/guest_videos";
  }

  @GetMapping("/videos/watch/{videoId}")
  public String watchUserVideo(
      @PathVariable Long videoId,
      @RequestParam(required = false) String commentSort,
       Model model) {

    Iterable<Video> listAllVideos = videoService.listAllVideos();
    Video video = videoService.getOneVideo(videoId);
    List<VideoComment> listAllCommentsByVideo = videoCommentService.listAllComments(videoId, commentSort);
    Integer countComments = videoCommentService.countCommentsByVideo(videoId);
    Integer countVideosByUser = videoService.countVideoByUser(video.getUser().getId());
    model.addAttribute("listAllVideos", listAllVideos);
    model.addAttribute("video", video);
    model.addAttribute("countVideosByUser", countVideosByUser);
    model.addAttribute("listAllCommentsByVideo", listAllCommentsByVideo);
    model.addAttribute("countComments", countComments);
    model.addAttribute("pageTitle", video.getTitle() + " | Circle");
    return "/guest/guest-single-video";
  }

  @GetMapping("/videos/userVideos/{userId}")
  public String userVideos(Model model, @PathVariable Long userId,
      @RequestParam(required = false) String mediaSort) {
    User user = userService.getUserById(userId);
    List<Video> listAllMyVideos = videoService.listAllMyVideos(userId, mediaSort);
    model.addAttribute("listAllMyVideos", listAllMyVideos);
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", user.getFullUserName() + " video list | Circle");
    return "/guest/guest_user_videos";
  }

  @GetMapping("/presentations")
  public String viewPresentations(Model model) {
    return listPresentationsPage(1, model, null, "createdAt", "desc");
  }

  @GetMapping("/presentations/page/{pageNum}")
  public String listPresentationsPage(@PathVariable int pageNum,
      Model model,
      String keyword,
      String sortField,
      String sortDir) {
    Common.listAllPresentationsPage(pageNum, model, keyword, sortField, sortDir, presentationService);
    return "/guest/guest_presentations";
  }

  @GetMapping("/presentations/watch/{presentationId}")
  public String watchUserPresentation(
      @PathVariable Long presentationId,
      @RequestParam(required = false) String commentSort,
      Model model) {

    Iterable<Presentation> listAllPresentations = presentationService.listAllPresentations();
    Presentation presentation = presentationService.getOnePresentation(presentationId);
    List<PresentationComment> listAllCommentsByPresentation = presentationCommentService.listAllComments(presentationId, commentSort);
    Integer countComments = presentationCommentService.countCommentsByPresentation(presentationId);
    Integer countPresentationsByUser = presentationService.countPresentationByUser(presentation.getUser().getId());
    model.addAttribute("listAllPresentations", listAllPresentations);
    model.addAttribute("presentation", presentation);
    model.addAttribute("countPresentationsByUser", countPresentationsByUser);
    model.addAttribute("listAllCommentsByPresentation", listAllCommentsByPresentation);
    model.addAttribute("countComments", countComments);
    model.addAttribute("pageTitle", presentation.getTitle() + " | Circle");
    return "/guest/guest-single-presentation";
  }

  @GetMapping("/presentations/userPresentations/{userId}")
  public String userPresentations(Model model, @PathVariable Long userId,
      @RequestParam(required = false) String mediaSort) {
    User user = userService.getUserById(userId);
    List<Presentation> listAllMyPresentations = presentationService.listAllMyPresentations(userId, mediaSort);
    model.addAttribute("listAllMyPresentations", listAllMyPresentations);
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", user.getFullUserName() + " Presentations list | Circle");
    return "/guest/guest_user_presentations";
  }

  @GetMapping("pictures")
  public String viewPictures(Model model) {
    return listPicturesPage(1, model, null, "createdAt", "desc");
  }

  @GetMapping("/pictures/page/{pageNum}")
  public String listPicturesPage(@PathVariable(name = "pageNum") int pageNum,
      Model model,
      String keyword,
      String sortField,
      String sortDir) {

    Common.listAllPicturesPage(pageNum, model, keyword, sortField, sortDir, pictureService);
    return "/guest/guest_pictures";
  }

  @GetMapping("/pictures/watch/{pictureId}")
  public String watchUserPicture(
      @PathVariable Long pictureId,
      @RequestParam(required = false) String commentSort,
      Model model) {

    Iterable<Picture> listAllPictures = pictureService.listAllPictures();
    Picture picture = pictureService.getOnePicture(pictureId);
    List<PictureComment> listAllCommentsByPicture = pictureCommentService.listAllComments(pictureId, commentSort);
    Integer countComments = pictureCommentService.countCommentsByPicture(pictureId);
    Integer countPicturesByUser = pictureService.countPictureByUser(picture.getUser().getId());
    model.addAttribute("listAllPictures", listAllPictures);
    model.addAttribute("picture", picture);
    model.addAttribute("countPicturesByUser", countPicturesByUser);
    model.addAttribute("listAllCommentsByPicture", listAllCommentsByPicture);
    model.addAttribute("countComments", countComments);
    model.addAttribute("pageTitle", picture.getTitle() + " | Circle");
    return "/guest/guest-single-picture";
  }

  @GetMapping("/pictures/userPictures/{userId}")
  public String userPictures(Model model, @PathVariable Long userId,
      @RequestParam(required = false) String mediaSort) {
    User user = userService.getUserById(userId);
    List<Picture> listAllMyPictures = pictureService.listAllMyPictures(userId, mediaSort);
    model.addAttribute("listAllMyPictures", listAllMyPictures);
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", user.getFullUserName() + " Pictures list | Circle");
    return "/guest/guest_user_pictures";
  }
}
