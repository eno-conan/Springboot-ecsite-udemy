package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

public class ProductSaveHelper {
	public static final String UPLOAD_BASE_DIR = "../product-images/";
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

	public static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues,
			Product product) {
		if (detailNames == null || detailNames.length == 0) {
			return;
		}

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);

			if (id != 0) {
				product.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
	}

	public static void deleteExtraImagesWeredRemovedOnForm(Product product) {
		String extraImageDir = UPLOAD_BASE_DIR + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		try {
			Files.list(dirPath).forEach(file -> {
				String fileName = file.toFile().getName();
				// compare extras folder target product and method paramter "product"
				if (!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Delete extra image:" + fileName);
					} catch (IOException ex1) {
						LOGGER.error("could not delete file:" + file);
					}
				}
			});
		} catch (IOException ex2) {
			LOGGER.error("could not delete file:" + dirPath);
		}
	}

	public static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length == 0) {
			return;
		}

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);

	}

	public static void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());// \と/の違いを吸収するっぽい
			String fileNameFilledBySnake = fileName.replace(" ", "_");
			product.setMainImage(fileNameFilledBySnake);
		}
	}

	public static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					String fileNameFilledBySnake = fileName.replace(" ", "_");
					if (!product.containsImageName(fileNameFilledBySnake)) {
						product.addExtraImage(fileNameFilledBySnake);
					}
				}
			}
		}
	}

	public static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedproduct) throws IOException {

		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String fileNameFilledBySnake = fileName.replace(" ", "_");
			String uploadDir = UPLOAD_BASE_DIR + savedproduct.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileNameFilledBySnake, mainImageMultipart);
			LOGGER.info("Uploaded MainImage:" + fileName);
		}

		if (extraImageMultiparts.length > 0) {
			String uploadDir = UPLOAD_BASE_DIR + savedproduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) {
					continue;
				}
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				String fileNameFilledBySnake = fileName.replace(" ", "_");
				FileUploadUtil.saveFile(uploadDir, fileNameFilledBySnake, multipartFile);
				LOGGER.info("Uploaded extraImage:" + fileName);
			}
		}
	}
}
