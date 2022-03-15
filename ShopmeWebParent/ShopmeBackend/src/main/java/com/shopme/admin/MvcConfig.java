package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registory) {

		// each segment pictures
		String userImageDirName = "user-photos";
		String categoriesImageDirName = "../category-images";
		String brandsImageDirName = "../brand-logos";
		String productsImageDirName = "../product-images";
		String siteLogoDirName = "../sute-logo";
		List<String> dirNames = composeImagesDirNameList(userImageDirName, categoriesImageDirName, brandsImageDirName,
				productsImageDirName, siteLogoDirName);
		for (String dirname : dirNames) {
			addResouceFromDirName(dirname, registory);
		}

	}

	private List<String> composeImagesDirNameList(String... dirNames) {
		List<String> dirNamesList = new ArrayList<>();
		for (String dirName : dirNames) {
			dirNamesList.add(dirName);
		}
		return dirNamesList;
	}

	private void addResouceFromDirName(String dirName, ResourceHandlerRegistry registory) {
		Path imageDir = Paths.get(dirName);
		String imagesPath = imageDir.toFile().getAbsolutePath();
		String logicalPath = dirName.replace("..", "") + "/**";
		registory.addResourceHandler(logicalPath).addResourceLocations("file:/" + imagesPath + "/");

	}

}
