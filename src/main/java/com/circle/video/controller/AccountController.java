package com.circle.video.controller;

import com.circle.video.exception.ForbiddenException;
import com.circle.video.model.Picture;
import com.circle.video.model.Presentation;
import com.circle.video.model.User;
import com.circle.video.model.Video;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.PictureService;
import com.circle.video.service.PresentationService;
import com.circle.video.service.UserService;
import com.circle.video.service.VideoService;
import com.circle.video.util.FileUploadUtil;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

  private final UserService userService;
  private final VideoService videoService;
  private final PresentationService presentationService;
  private final PictureService pictureService;

  @GetMapping("")
  public String viewDetails(@AuthenticationPrincipal CircleUserDetails loggedUser, Model model) {
    String username = loggedUser.getUsername();
    User user = userService.getUserByUserName(username);
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", user.getFullUserName() + " account details");
    return "account_form";
  }

  @PostMapping("/update")
  public String saveDetails(User user, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CircleUserDetails loggedUser, @RequestParam("image") MultipartFile multipartFile) throws IOException {
    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
      user.setProfileImage(fileName);
      User savedUser = userService.updateAccount(user);
      String uploadDir = "media/user-profile-image/" + savedUser.getId();
      FileUploadUtil.cleanDir(uploadDir);
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    } else {
      if (user.getProfileImage().isEmpty()) {
        user.setProfileImage(null);
      }
      userService.updateAccount(user);
    }
    loggedUser.setProfileImage(user.getProfileImage());
    redirectAttributes.addFlashAttribute("message", "Your account details have been updated!");

    return "redirect:/account";
  }

  @GetMapping("/myVideos")
  public String myVideos(Model model, @AuthenticationPrincipal CircleUserDetails loggedUser,
      @RequestParam(name = "mediaSort", required = false) String mediaSort) {
    Long userId = loggedUser.getId();
    List<Video> listAllMyVideos = videoService.listAllMyVideos(userId, mediaSort);
    model.addAttribute("listAllMyVideos", listAllMyVideos);
    model.addAttribute("pageTitle", "My Videos list | Circle");
    return "my_videos";
  }

  @GetMapping("/myVideos/delete/{videoId}")
  public String deleteMyVideo(@PathVariable(name = "videoId") Long videoId,
      RedirectAttributes redirectAttributes, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long videoUserId = loggedUser.getId();
    Video video = videoService.getOneVideo(videoId);
    String videoTitle = video.getTitle();
    Long loggedUserId = video.getUser().getId();
    if(loggedUserId.equals(videoUserId)) {
      videoService.delete(videoId);
      String subVideoDir = "media/user-videos/" + loggedUserId + "/" + videoId;
      FileUploadUtil.removeDir(subVideoDir);
      redirectAttributes.addFlashAttribute("message", videoTitle + " has been deleted successfully!");
    } else {
      throw new ForbiddenException("You don't have permission to delete this Video!");
    }
    return "redirect:/account/myVideos";
  }

  @GetMapping("/myPresentations")
  public String myPresentations(Model model, @AuthenticationPrincipal CircleUserDetails loggedUser,
      @RequestParam(name = "mediaSort", required = false) String mediaSort) { ;
    Long userId = loggedUser.getId();
    List<Presentation> listAllPresentations = presentationService.listAllMyPresentations(userId, mediaSort);
    model.addAttribute("listAllPresentations", listAllPresentations);
    model.addAttribute("pageTitle", "My Presentations list | Circle");
    return "my_presentations";
  }

  @GetMapping("/myPresentations/delete/{presentationId}")
  public String deleteMyPresentation(@PathVariable(name = "presentationId") Long presentationId,
      RedirectAttributes redirectAttributes, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long presentationUserId = loggedUser.getId();
    Presentation presentation = presentationService.getOnePresentation(presentationId);
    String presentationTitle = presentation.getTitle();
    Long loggedUserId = presentation.getUser().getId();
    if(loggedUserId.equals(presentationUserId)) {
      presentationService.delete(presentationId);
      String subPresentationDir = "media/user-presentations/" + loggedUserId + "/" + presentationId;
      FileUploadUtil.removeDir(subPresentationDir);
      redirectAttributes.addFlashAttribute("message", presentationTitle + " has been deleted successfully!");
    } else {
      throw new ForbiddenException("You don't have permission to delete this Presentation!");
    }
    return "redirect:/account/myPresentations";
  }

  @GetMapping("/myPictures")
  public String myPictures(Model model, @AuthenticationPrincipal CircleUserDetails loggedUser,
      @RequestParam(name = "mediaSort", required = false) String mediaSort) {
    Long userId = loggedUser.getId();
    List<Picture> listAllPictures = pictureService.listAllMyPictures(userId, mediaSort);
    model.addAttribute("listAllPictures", listAllPictures);
    model.addAttribute("pageTitle",  "My Pictures list | Circle");
    return "my_pictures";
  }

  @GetMapping("/myPictures/delete/{pictureId}")
  public String deleteMyPicture(@PathVariable(name = "pictureId") Long pictureId,
      RedirectAttributes redirectAttributes, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    Long pictureUserId = loggedUser.getId();
    Picture picture = pictureService.getOnePicture(pictureId);
    String pictureTitle = picture.getTitle();
    Long loggedUserId = picture.getUser().getId();
    if(loggedUserId.equals(pictureUserId)) {
      pictureService.delete(pictureId);
      String subPictureDir = "media/user-pictures/" + loggedUserId + "/" + pictureId;
      FileUploadUtil.removeDir(subPictureDir);
      redirectAttributes.addFlashAttribute("message", pictureTitle + " has been deleted successfully!");
    } else {
      throw new ForbiddenException("You don't have permission to delete this Picture!");
    }
    return "redirect:/account/myPictures";
  }

}
