package com.circle.video.controller;

import com.circle.video.exception.EntityNotFoundException;
import com.circle.video.exception.ForbiddenException;
import com.circle.video.model.Category;
import com.circle.video.model.VideoComment;
import com.circle.video.model.User;
import com.circle.video.model.Video;
import com.circle.video.security.CircleUserDetails;
import com.circle.video.service.CategoryService;
import com.circle.video.service.VideoCommentService;
import com.circle.video.service.UserService;
import com.circle.video.service.VideoService;
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
@RequestMapping("/videos")
public class VideoController {

  private final UserService userService;
  private final VideoService videoService;
  private final VideoCommentService videoCommentService;
  private final CategoryService categoryService;

  @GetMapping("")
  public String viewVideos(Model model) {
    return listVideosPage(1, model, null, "createdAt", "desc");
  }

  @GetMapping("/page/{pageNum}")
  public String listVideosPage(@PathVariable int pageNum,
      Model model,
      String keyword,
      String sortField,
      String sortDir) {

    Common.listAllVideosPage(pageNum, model, keyword, sortField, sortDir, videoService);
    return "videos";
  }

  @GetMapping("/new")
  public String videoUpload(Model model) {
    Video video = new Video();
    List<Category> listCategories = categoryService.listAllCategories();
    model.addAttribute("listCategories", listCategories);
    model.addAttribute("video", video);
    return "upload_video_form";
  }

  @PostMapping("/save")
  public String saveVideo(Video video,
      @AuthenticationPrincipal CircleUserDetails loggedUser,
      @RequestParam("media_file") MultipartFile multipartFileMedia,
      @RequestParam("thumbnail_file") MultipartFile multipartFileThumbnail,
      RedirectAttributes redirectAttributes) throws IOException {

    User user = userService.getUserByUserName(loggedUser.getUsername());
    String fileNameVideo = StringUtils.cleanPath(Objects.requireNonNull(multipartFileMedia.getOriginalFilename()));
    LocalDateTime now = LocalDateTime.now();
    video.setVideoFile(fileNameVideo);
    video.setCreatedAt(now);
    video.setUser(user);
    Video savedVideo = videoService.save(video);
    String uploadDirVideo = "media/user-videos/" + user.getId() + "/" + savedVideo.getId();
    FileUploadUtil.saveFile(uploadDirVideo, fileNameVideo, multipartFileMedia);
    if (!multipartFileThumbnail.isEmpty()) {
      String fileNameThumbnail = StringUtils.cleanPath(Objects.requireNonNull(multipartFileThumbnail.getOriginalFilename()));
      video.setVideoThumbnail(fileNameThumbnail);
      FileUploadUtil.saveFile(uploadDirVideo, fileNameThumbnail, multipartFileThumbnail);
    }
    videoService.save(video);
    String videoTitle = video.getTitle();
    redirectAttributes.addFlashAttribute("message", videoTitle + " has been uploaded successfully!");
    return "redirect:/account/myVideos";
  }

  @GetMapping("/watch/{videoId}")
  public String watchUserVideo(@AuthenticationPrincipal CircleUserDetails loggedUser,
      @PathVariable Long videoId,
      @RequestParam(required = false) String commentSort,
      VideoComment newComment,
      Model model) {
    User loggedInUser = userService.getUserById(loggedUser.getId());
    Iterable<Video> listAllVideos = videoService.listAllVideos();
    Video video = videoService.getOneVideo(videoId);
    List<VideoComment> listAllCommentsByVideo = videoCommentService.listAllComments(videoId, commentSort);
    Integer countComments = videoCommentService.countCommentsByVideo(videoId);
    Integer countVideosByUser = videoService.countVideoByUser(video.getUser().getId());
    model.addAttribute("listAllVideos", listAllVideos);
    model.addAttribute("video", video);
    model.addAttribute("countVideosByUser", countVideosByUser);
    model.addAttribute("newComment", newComment);
    model.addAttribute("listAllCommentsByVideo", listAllCommentsByVideo);
    model.addAttribute("countComments", countComments);
    model.addAttribute("loggedInUser", loggedInUser);
    model.addAttribute("pageTitle", video.getTitle() + " | Circle");
    return "single-video";
  }

  @GetMapping("/userVideos/{userId}")
  public String userVideos(Model model, @PathVariable Long userId,
      @RequestParam(required = false) String mediaSort) {
    User user = userService.getUserById(userId);
    List<Video> listAllMyVideos = videoService.listAllMyVideos(userId, mediaSort);
    model.addAttribute("listAllMyVideos", listAllMyVideos);
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", user.getFullUserName() + " video list | Circle");
    return "user_videos";
  }

  @GetMapping("/userVideos/delete/{videoId}")
  public String deleteUserVideo(@PathVariable Long videoId, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CircleUserDetails loggedUser) {
    String loggedUserRoleName = loggedUser.getRoleName();
    Video video = videoService.getOneVideo(videoId);
    String videoTitle = video.getTitle();
    Long userId = video.getUser().getId();
    try {
      if (loggedUserRoleName.equals("Assistant") || loggedUserRoleName.equals("Admin")) {
        videoService.delete(videoId);
        String subVideoDir = "media/user-videos/" + userId + "/" + videoId;
        FileUploadUtil.removeDir(subVideoDir);
        redirectAttributes.addFlashAttribute("message", videoTitle + " has been deleted successfully!");
      } else {
        throw new ForbiddenException("You don't have permission to delete this Video!");
      }
    } catch (EntityNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }
    return "redirect:/videos/userVideos/" + userId;
  }
}
