package com.circle.video.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String dirProfile = "media/user-profile-image";
    String dirVideos = "media/user-videos";
    String dirPictures = "media/user-pictures";
    String dirPresentations = "media/user-presentations";
    Path userProfilesDir = Paths.get(dirProfile);
    Path userVideosDir = Paths.get(dirVideos);
    Path userPicturesDir = Paths.get(dirPictures);
    Path userPresentationsDir = Paths.get(dirPresentations);
    String userProfilesPath = userProfilesDir.toFile().getAbsolutePath();
    String userVideosPath = userVideosDir.toFile().getAbsolutePath();
    String userPicturesPath = userPicturesDir.toFile().getAbsolutePath();
    String userPresentationsPath = userPresentationsDir.toFile().getAbsolutePath();
    registry.addResourceHandler("/" + dirProfile + "/**").addResourceLocations("file:/" + userProfilesPath + "/");
    registry.addResourceHandler("/" + dirVideos + "/**").addResourceLocations("file:/" + userVideosPath + "/");
    registry.addResourceHandler("/" + dirPictures + "/**").addResourceLocations("file:/" + userPicturesPath + "/");
    registry.addResourceHandler("/" + dirPresentations + "/**").addResourceLocations("file:/" + userPresentationsPath + "/");
  }
}
