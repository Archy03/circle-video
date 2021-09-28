package com.circle.video.controller;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.exception.ForbiddenException;
import com.circle.video.model.Category;
import com.circle.video.model.Presentation;
import com.circle.video.model.PresentationComment;
import com.circle.video.model.User;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.CategoryService;
import com.circle.video.service.PresentationCommentService;
import com.circle.video.service.PresentationService;
import com.circle.video.service.UserService;
import com.circle.video.util.Common;
import com.circle.video.util.FileUploadUtil;
import java.io.IOException;
import java.time.LocalDateTime;
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
@RequestMapping("/presentations")
public class PresentationController {

  private final UserService userService;
  private final PresentationService presentationService;
  private final PresentationCommentService presentationCommentService;
  private final CategoryService categoryService;

  @GetMapping("")
  public String viewPresentations(Model model) {
    return listPresentationsPage(1, model, null, "createdAt", "desc");
  }

  @GetMapping("/page/{pageNum}")
  public String listPresentationsPage(@PathVariable int pageNum,
      Model model,
      String keyword,
      String sortField,
      String sortDir) {
    Common.listAllPresentationsPage(pageNum, model, keyword, sortField, sortDir, presentationService);
    return "presentations";
  }

  @GetMapping("/new")
  public String presentationUpload(Model model) {
    Presentation presentation = new Presentation();
    List<Category> listCategories = categoryService.listAllCategories();
    model.addAttribute("listCategories", listCategories);
    model.addAttribute("presentation", presentation);
    model.addAttribute("pageTitle", "Upload Presentation | Circle");
    return "upload_presentation_form";
  }

  @PostMapping("/save")
  public String savePresentation(Presentation presentation,
      @AuthenticationPrincipal CircleUserDetails loggedUser,
      @RequestParam("media_file") MultipartFile multipartFileMedia,
      @RequestParam("thumbnail_file") MultipartFile multipartFileThumbnail,
      RedirectAttributes redirectAttributes) throws IOException {

    User user = userService.getUserByUserName(loggedUser.getUsername());
    String fileNameMedia = StringUtils.cleanPath(Objects.requireNonNull(multipartFileMedia.getOriginalFilename()));
    LocalDateTime now = LocalDateTime.now();
    presentation.setPresentationFile(fileNameMedia);
    presentation.setCreatedAt(now);
    presentation.setUser(user);
    Presentation savedPresentation = presentationService.save(presentation);
    String uploadDirPresentation = "media/user-presentations/" + user.getId() + "/" + savedPresentation.getId();
    FileUploadUtil.saveFile(uploadDirPresentation, fileNameMedia, multipartFileMedia);
    if (!multipartFileThumbnail.isEmpty()) {
      String fileNameThumbnail = StringUtils.cleanPath(Objects.requireNonNull(multipartFileThumbnail.getOriginalFilename()));
      presentation.setPresentationThumbnail(fileNameThumbnail);
      FileUploadUtil.saveFile(uploadDirPresentation, fileNameThumbnail, multipartFileThumbnail);
    }
    presentationService.save(presentation);
    String presentationTitle = presentation.getTitle();
    redirectAttributes.addFlashAttribute("message", presentationTitle + " has been uploaded successfully!");
    return "redirect:/account/myPresentations";
  }

  @GetMapping("/watch/{presentationId}")
  public String watchUserPresentation(@AuthenticationPrincipal CircleUserDetails loggedUser,
      @PathVariable Long presentationId,
      @RequestParam(required = false) String commentSort,
      PresentationComment newComment,
      Model model) {

    User loggedInUser = userService.getUserById(loggedUser.getId());
    Iterable<Presentation> listAllPresentations = presentationService.listAllPresentations();
    Presentation presentation = presentationService.getOnePresentation(presentationId);
    List<PresentationComment> listAllCommentsByPresentation = presentationCommentService.listAllComments(presentationId, commentSort);
    Integer countComments = presentationCommentService.countCommentsByPresentation(presentationId);
    Integer countPresentationsByUser = presentationService.countPresentationByUser(presentation.getUser().getId());
    model.addAttribute("listAllPresentations", listAllPresentations);
    model.addAttribute("presentation", presentation);
    model.addAttribute("countPresentationsByUser", countPresentationsByUser);
    model.addAttribute("newComment", newComment);
    model.addAttribute("listAllCommentsByPresentation", listAllCommentsByPresentation);
    model.addAttribute("countComments", countComments);
    model.addAttribute("loggedInUser", loggedInUser);
    model.addAttribute("pageTitle", presentation.getTitle() + " | Circle");
    return "single-presentation";
  }

  @GetMapping("/userPresentations/{userId}")
  public String userPresentations(Model model, @PathVariable Long userId,
      @RequestParam(required = false) String mediaSort) {
    User user = userService.getUserById(userId);
    List<Presentation> listAllMyPresentations = presentationService.listAllMyPresentations(userId, mediaSort);
    model.addAttribute("listAllMyPresentations", listAllMyPresentations);
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", user.getFullUserName() + " Presentations list | Circle");
    return "user_presentations";
  }


  @GetMapping("/userPresentations/delete/{presentationId}")
  public String deleteUserVideo(@PathVariable Long presentationId,
      RedirectAttributes redirectAttributes,
      @AuthenticationPrincipal CircleUserDetails loggedUser) {

    String loggedUserRoleName = loggedUser.getRoleName();
    Presentation presentation = presentationService.getOnePresentation(presentationId);
    String presentationTitle = presentation.getTitle();
    Long userId = presentation.getUser().getId();
    try {
      if (loggedUserRoleName.equals("Assistant") || loggedUserRoleName.equals("Admin")) {
        presentationService.delete(presentationId);
        String subPresentationDir = "media/user-presentations/" + userId + "/" + presentationId;
        FileUploadUtil.removeDir(subPresentationDir);
        redirectAttributes.addFlashAttribute("message", presentationTitle + " has been deleted successfully!");
      } else {
        throw new ForbiddenException("You don't have permission to delete this Presentation!");
      }
    } catch (EntityNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }
    return "redirect:/presentations/userPresentations/" + userId;
  }
}
