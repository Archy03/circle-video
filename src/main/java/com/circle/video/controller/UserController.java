package com.circle.video.controller;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.model.Picture;
import com.circle.video.model.Presentation;
import com.circle.video.model.Role;
import com.circle.video.model.User;
import com.circle.video.model.Video;
import com.circle.video.service.PictureCommentService;
import com.circle.video.service.PictureRatingService;
import com.circle.video.service.PictureService;
import com.circle.video.service.PresentationCommentService;
import com.circle.video.service.PresentationRatingService;
import com.circle.video.service.PresentationService;
import com.circle.video.service.VideoCommentService;
import com.circle.video.service.VideoRatingService;
import com.circle.video.service.UserService;
import com.circle.video.service.VideoService;
import com.circle.video.util.Common;
import com.circle.video.util.FileUploadUtil;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  private final VideoService videoService;
  private final VideoCommentService videoCommentService;
  private final VideoRatingService videoRatingService;

  private final PresentationService presentationService;
  private final PresentationCommentService presentationCommentService;
  private final PresentationRatingService presentationRatingService;

  private final PictureService pictureService;
  private final PictureCommentService pictureCommentService;
  private final PictureRatingService pictureRatingService;

  @GetMapping("/users")
  public String listFirstPage(Model model) {
    return listByPage(1, model, "firstName", "asc", null);
  }

  @GetMapping("/users/page/{pageNum}")
  public String listByPage(@PathVariable int pageNum,
      Model model,
      String sortField,
      String sortDir,
      String keyword) {

    Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
    List<User> listUsers = page.getContent();
    Common.sortingRecordPage(pageNum, model, sortDir, sortField, keyword, page.getTotalElements(), page.getTotalPages());
    model.addAttribute("listUsers", listUsers);
    return "users";
  }

  @GetMapping("/users/new_user")
  public String newUser(Model model) {
    List<Role> listRoles = userService.listRoles();
    User user = new User();
    user.setEnabled(true);
    model.addAttribute("user", user);
    model.addAttribute("listRoles", listRoles);
    model.addAttribute("pageTitle", "Create New User");
    return "new_user";
  }

  @PostMapping("/users/save")
  public String saveUser(User user,
      RedirectAttributes redirectAttributes,
      @RequestParam("image") MultipartFile multipartFile) throws IOException {
    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
      user.setProfileImage(fileName);
      User savedUser = userService.save(user);
      String uploadDir = "media/user-profile-image/" + savedUser.getId();
      FileUploadUtil.cleanDir(uploadDir);
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    } else {
      if (user.getProfileImage().isEmpty()) {
        user.setProfileImage(null);
      }
      userService.save(user);
    }
    redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");
    return getRedirectedURLtoAffectedUser(user);
  }

  private String getRedirectedURLtoAffectedUser(User user) {
    String userName = user.getUserName();
    return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + userName;
  }

  @GetMapping("/users/edit/{id}")
  public String editUser(@PathVariable Long id, Model model) {
      User user = userService.getUserById(id);
      List<Role> listRoles = userService.listRoles();
      model.addAttribute("user", user);
      model.addAttribute("pageTitle", "Edit user: " + user.getFullUserName());
      model.addAttribute("listRoles", listRoles);
      return "new_user";
  }

  @GetMapping("/users/delete/{id}")
  public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
      User user = userService.getUserById(id);
      String userDir = "media/user-profile-image/" + id;
      String videoDir = "media/user-videos/" + id;
      String presentationDir = "media/user-presentations/" + id;
      String pictureDir = "media/user-pictures/" + id;
      videoRatingService.deleteAllUserRatings(id);
      videoCommentService.deleteAllUserComments(id);
      presentationRatingService.deleteAllUserRatings(id);
      presentationCommentService.deleteAllUserComments(id);
      pictureRatingService.deleteAllUserRatings(id);
      pictureCommentService.deleteAllUserComments(id);
      if (!videoService.listAllVideosByUser(id).isEmpty()) {
        for (Video video: videoService.listAllVideosByUser(id)) {
          FileUploadUtil.removeDir(videoDir + "/" + video.getId());
        }
        FileUploadUtil.removeDir(videoDir);
        videoService.deleteAllUserVideos(id);
      }
      if (!presentationService.listAllPresentationsByUser(id).isEmpty()) {
        for (Presentation presentation: presentationService.listAllPresentationsByUser(id)) {
          FileUploadUtil.removeDir(presentationDir + "/" + presentation.getId());
        }
        FileUploadUtil.removeDir(presentationDir);
        presentationService.deleteAllUserPresentations(id);
      }
      if (!pictureService.listAllPicturesByUser(id).isEmpty()) {
        for (Picture picture: pictureService.listAllPicturesByUser(id)) {
          FileUploadUtil.removeDir(pictureDir + "/" + picture.getId());
        }
        FileUploadUtil.removeDir(pictureDir);
        pictureService.deleteAllUserPictures(id);
      }
      if (user.getProfileImage() != null) {
        FileUploadUtil.removeDir(userDir);
      }
      userService.delete(id);
      redirectAttributes.addFlashAttribute("message", user.getFullUserName() + " has been deleted successfully;");
    } catch (EntityNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }
    return "redirect:/users";
    //return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + userName;
  }

}
