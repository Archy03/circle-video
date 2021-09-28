package com.circle.video.util;

import com.circle.video.model.Element;
import com.circle.video.model.Picture;
import com.circle.video.model.Presentation;
import com.circle.video.model.Video;
import com.circle.video.service.PictureService;
import com.circle.video.service.PresentationService;
import com.circle.video.service.VideoService;
import java.util.List;
import javax.persistence.Transient;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public class Common {

  public static void sortingRecordPage(
      int pageNum,
      Model model,
      String sortDir,
      String sortField,
      String keyword,
      long totalElements,
      int totalPages) {

    int startCount = (pageNum - 1) * Element.ITEMS_PER_PAGE.getValue() + 1;
    long endCount = startCount + Element.ITEMS_PER_PAGE.getValue() - 1;
    if (endCount > totalElements) {
      endCount = totalElements;
    }
    String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("startCount", startCount);
    model.addAttribute("endCount", endCount);
    model.addAttribute("totalItems", totalElements);
    model.addAttribute("reverseSortDir", reverseSortDir);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("sortField", sortField);
    model.addAttribute("keyword", keyword);
  }

  public static void listAllVideosPage(@PathVariable int pageNum,
      Model model,
      String keyword,
      String sortField,
      String sortDir,
      VideoService videoService) {
    Page<Video> page = videoService.listAllVideosPage(pageNum, sortField, sortDir, keyword);
    List<Video> listVideos = page.getContent();
    sortingRecordPage(pageNum, model, sortDir, sortField, keyword, page.getTotalElements(), page.getTotalPages());
    model.addAttribute("listVideos", listVideos);
    model.addAttribute("pageTitle", "Videos list | Circle");
  }

  public static void listAllPresentationsPage(@PathVariable int pageNum, Model model, String keyword, String sortField, String sortDir, PresentationService presentationService) {
    Page<Presentation> page = presentationService.listAllPresentationsPage(pageNum, sortField, sortDir, keyword);
    List<Presentation> listPresentations = page.getContent();
    sortingRecordPage(pageNum, model, sortDir, sortField, keyword, page.getTotalElements(), page.getTotalPages());
    model.addAttribute("listPresentations", listPresentations);
    model.addAttribute("pageTitle", "Presentation list | Circle");
  }

  public static void listAllPicturesPage(@PathVariable(name = "pageNum") int pageNum, Model model, String keyword, String sortField, String sortDir, PictureService pictureService) {
    Page<Picture> page = pictureService.listAllPicturesPage(pageNum, sortField, sortDir, keyword);
    List<Picture> listPictures = page.getContent();
    Common.sortingRecordPage(pageNum, model, sortDir, sortField, keyword, page.getTotalElements(), page.getTotalPages());
    model.addAttribute("listPictures", listPictures);
    model.addAttribute("pageTitle", "Picture list | Circle");
  }


  public static String likeDislikePercent(int likeSum, int dislikeSum) {
    int likeAndDislikeSum = likeSum + dislikeSum;
    if(dislikeSum == 0 && likeSum == 0) {
      return "50";
    }
    if(dislikeSum == 0 && likeSum > 0) {
      return "100";
    }
    if(dislikeSum > 0 && likeSum == 0) {
      return "0";
    }
    float calcRatingPercent = (float)likeSum / likeAndDislikeSum * 100;
    return String.format("%.1f", calcRatingPercent);
  }
}
