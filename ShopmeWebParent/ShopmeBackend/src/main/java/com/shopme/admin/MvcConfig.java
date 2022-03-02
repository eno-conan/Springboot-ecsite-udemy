package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shopme.admin.user.controller.UserController;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registory) {

		String dirName = UserController.UPLOAD_BASE_DIR;

		Path userPhotosdir = Paths.get(dirName);

		String userPhotosPath = userPhotosdir.toFile().getAbsolutePath();
//		System.out.println(userPhotosPath);
		registory.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + userPhotosPath + "/");

		//each segment pictures
		String categoriesImageDirName = "../category-images";
		String brandsImageDirName = "../brand-logos";

		Path categoriesImageDir = Paths.get(categoriesImageDirName);
		Path brandsImageDir = Paths.get(brandsImageDirName);

		String categoriesImagesPath = categoriesImageDir.toFile().getAbsolutePath();
		String brandsImagesPath = brandsImageDir.toFile().getAbsolutePath();
//		System.out.println(userPhotosPath);
		registory.addResourceHandler("/category-images/**").addResourceLocations("file:/" + categoriesImagesPath + "/");
		registory.addResourceHandler("/brand-logos/**").addResourceLocations("file:/" + brandsImagesPath + "/");

	}

}
