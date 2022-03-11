package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {

	public static final String UPLOAD_BASE_DIR = "../brand-logos";

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/brands")
	public String listAll(Model model) {
		List<Brand> brands = brandService.listAll();
		model.addAttribute("brands", brands);
		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String newCategory(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		model.addAttribute("brand", new Brand());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Brand");
		return "brands/brand_form";

	}

	@PostMapping("/brands/save")
	public String saveCategory(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());// \と/の違いを吸収するっぽい
		String fileNameFilledBySnake = fileName.replace(" ", "_");
		brand.setLogo(fileNameFilledBySnake);
		Brand savedBrand = brandService.save(brand);
		String uploadDir = UPLOAD_BASE_DIR + "/" + savedBrand.getId();
		// 画像を更新する場合、既存の画像は削除
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileNameFilledBySnake, multipartFile);
		redirectAttributes.addFlashAttribute("msg", "The Brand has been saved successfully");
		return "redirect:/brands";

	}

//	@GetMapping("/categories/page/{pageNum}")
//	public String listAllByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
//			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
//
//		Page<Category> page = service.listAllByPage(pageNum, sortField, sortDir, keyword);
//		List<Category> categories = page.getContent();
//
//		// 何件目～何件目までを表示
//		long startCount = (pageNum - 1) * BrandService.CATEGORIES_PER_PAGE + 1;
//		long endCount = startCount + BrandService.CATEGORIES_PER_PAGE - 1;
//		if (endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//
//		// 昇順に並び替えしたら、降順にできるように。その逆も同様
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", page.getTotalElements());
//		model.addAttribute("categories", categories);
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword", keyword);
//		return "categories/categories";
//	}
//

}
