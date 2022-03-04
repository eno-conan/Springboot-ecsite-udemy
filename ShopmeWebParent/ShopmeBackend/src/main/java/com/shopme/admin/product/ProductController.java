package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {

	public static final String UPLOAD_BASE_DIR = "../product-images";

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BrandService brandService;

	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> products = productService.listAll();
		model.addAttribute("products", products);
		return "products/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println(id);
		return "index";
	}
//	memo:id,main-image,product name,brand,category,enabled,excel-edit-delete

	@GetMapping("/products/new")
	public String newCategory(Model model) {
		List<Brand> brands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("brands", brands);
		model.addAttribute("product", product);
		model.addAttribute("pageTitle", "Create New Products");
		return "products/product_form";

	}

//
	@PostMapping("/products/save")
	public String saveCategory(Product product, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile mainImageMultipart,
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts) throws IOException {

		setMainImageName(mainImageMultipart, product);
		setExtraImageNames(extraImageMultiparts, product);

		Product savedproduct = productService.save(product);

		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedproduct);

		redirectAttributes.addFlashAttribute("msg", "The Product has been saved successfully");
		return "redirect:/products";

	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());// \と/の違いを吸収するっぽい
			product.setMainImage(fileName);
		}
	}

	private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					product.addExtraImage(fileName);
				}
			}
		}
	}

	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedproduct) throws IOException {

		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = UPLOAD_BASE_DIR + "/" + savedproduct.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}

		if (extraImageMultiparts.length > 0) {
			String uploadDir = UPLOAD_BASE_DIR + "/" + savedproduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) {
					continue;
				}
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
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