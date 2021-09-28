package com.circle.video.controller;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.exception.ForbiddenException;
import com.circle.video.model.Category;
import com.circle.video.model.Picture;
import com.circle.video.model.PictureComment;
import com.circle.video.model.User;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.CategoryService;
import com.circle.video.service.PictureCommentService;
import com.circle.video.service.PictureService;
import com.circle.video.service.UserService;
import com.circle.video.util.Common;
import com.circle.video.util.FileUploadUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/pictures")
public class PictureController {

  private final UserService userService;
  private final PictureService pictureService;
  private final PictureCommentService pictureCommentService;
  private final CategoryService categoryService;

  @GetMapping("")
  public String viewPictures(Model model) {
    return listPicturesPage(1, model, null, "createdAt", "desc");
  }

  @GetMapping("/page/{pageNum}")
  public String listPicturesPage(@PathVariable(name = "pageNum") int pageNum,
      Model model,
      String keyword,
      String sortField,
      String sortDir) {

    Common.listAllPicturesPage(pageNum, model, keyword, sortField, sortDir, pictureService);
    return "pictures";
  }

  @GetMapping("/new")
  public String pictureUpload(Model model) {
    Picture picture = new Picture();
    List<Category> listCategories = categoryService.listAllCategories();
    model.addAttribute("listCategories", listCategories);
    model.addAttribute("picture", picture);
    model.addAttribute("pageTitle", "Upload Picture | Circle");
    return "upload_picture_form";
  }

  @PostMapping("/save")
  public String savePicture(Picture picture,
      @AuthenticationPrincipal CircleUserDetails loggedUser,
      @RequestParam("media_file") MultipartFile multipartFileMedia,
      RedirectAttributes redirectAttributes) throws IOException {

    User user = userService.getUserByUserName(loggedUser.getUsername());
    String fileNameMedia = StringUtils.cleanPath(Objects.requireNonNull(multipartFileMedia.getOriginalFilename()));
    LocalDateTime now = LocalDateTime.now();
    picture.setPictureFile(fileNameMedia);
    picture.setCreatedAt(now);
    picture.setUser(user);
    Picture savedPicture = pictureService.save(picture);
    String uploadDirPicture = "media/user-pictures/" + user.getId() + "/" + savedPicture.getId();
    FileUploadUtil.saveFile(uploadDirPicture, fileNameMedia, multipartFileMedia);
    pictureService.save(picture);
    String pictureTitle = picture.getTitle();
    redirectAttributes.addFlashAttribute("message", pictureTitle + " has been uploaded successfully!");
    return "redirect:/account/myPictures";
  }

  @GetMapping("/watch/{pictureId}")
  public String watchUserPicture(@AuthenticationPrincipal CircleUserDetails loggedUser,
      @PathVariable Long pictureId,
      @RequestParam(required = false) String commentSort,
      PictureComment newComment,
      Model model) {

    User loggedInUser = userService.getUserById(loggedUser.getId());
    Iterable<Picture> listAllPictures = pictureService.listAllPictures();
    Picture picture = pictureService.getOnePicture(pictureId);
    List<PictureComment> listAllCommentsByPicture = pictureCommentService.listAllComments(pictureId, commentSort);
    Integer countComments = pictureCommentService.countCommentsByPicture(pictureId);
    Integer countPicturesByUser = pictureService.countPictureByUser(picture.getUser().getId());
    model.addAttribute("listAllPictures", listAllPictures);
    model.addAttribute("picture", picture);
    model.addAttribute("countPicturesByUser", countPicturesByUser);
    model.addAttribute("newComment", newComment);
    model.addAttribute("listAllCommentsByPicture", listAllCommentsByPicture);
    model.addAttribute("countComments", countComments);
    model.addAttribute("loggedInUser", loggedInUser);
    model.addAttribute("pageTitle", picture.getTitle() + " | Circle");
    return "single-picture";
  }

  @GetMapping("/userPictures/{userId}")
  public String userPictures(Model model, @PathVariable Long userId,
      @RequestParam(required = false) String mediaSort) {
    User user = userService.getUserById(userId);
    List<Picture> listAllMyPictures = pictureService.listAllMyPictures(userId, mediaSort);
    model.addAttribute("listAllMyPictures", listAllMyPictures);
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", user.getFullUserName() + " Pictures list | Circle");
    return "user_pictures";
  }


  @GetMapping("/userPictures/delete/{pictureId}")
  public String deleteUserVideo(@PathVariable(name = "pictureId") Long pictureId,
      RedirectAttributes redirectAttributes,
      @AuthenticationPrincipal CircleUserDetails loggedUser) {

    String loggedUserRoleName = loggedUser.getRoleName();
    Picture picture = pictureService.getOnePicture(pictureId);
    String pictureTitle = picture.getTitle();
    Long userId = picture.getUser().getId();
    try {
      if (loggedUserRoleName.equals("Assistant") || loggedUserRoleName.equals("Admin")) {
        pictureService.delete(pictureId);
        String subPictureDir = "media/user-pictures/" + userId + "/" + pictureId;
        FileUploadUtil.removeDir(subPictureDir);
        redirectAttributes.addFlashAttribute("message", pictureTitle + " has been deleted successfully!");
      } else {
        throw new ForbiddenException("You don't have permission to delete this Picture!");
      }
    } catch (EntityNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }
    return "redirect:/pictures/userPictures/" + userId;
  }
}
