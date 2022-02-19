package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {

	public static final String UPLOAD_BASE_DIR = "user-photos";

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listAll(Model model) {
		final int INIT_PAGE = 1;
		// 1ページ目を表示するように設定
		return listAllByPage(INIT_PAGE, model);

	}

	@GetMapping("/users/page/{pageNum}")
	public String listAllByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<User> page = service.listAllByPage(pageNum);
		List<User> users = page.getContent();

		// 何件目～何件目までを表示
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("users", users);
		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		user.setEnabled(true);
		List<Role> roles = service.listRoles();
		model.addAttribute("roles", roles);
		model.addAttribute("user", user);
		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
//		System.out.println(user);

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());// \と/の違いを吸収するっぽい
			System.out.println(fileName);
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			String uploadDir = UPLOAD_BASE_DIR + "/" + savedUser.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}

		redirectAttributes.addFlashAttribute("msg", "success create new user");
		return "redirect:/users"; // usersのトップページにリダイレクト（再送信を防ぐ目的もあり）
	}

}
