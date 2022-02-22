package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
		// 1ページ目を表示するように設定（初期表示時、検索条件ないため、keywordに紐づく引数：null）
		return listAllByPage(INIT_PAGE, model, "firstName", "asc", null);

	}

	@GetMapping("/users/page/{pageNum}")
	public String listAllByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<User> page = service.listAllByPage(pageNum, sortField, sortDir, keyword);
		List<User> users = page.getContent();

		// 何件目～何件目までを表示
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		// 昇順に並び替えしたら、降順にできるように。その逆も同様
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("users", users);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		user.setEnabled(true);
		List<Role> roles = service.listRoles();
		model.addAttribute("pageTitle", "Create New User");
		model.addAttribute("roles", roles);
		model.addAttribute("user", user);
		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());// \と/の違いを吸収するっぽい
			System.out.println(fileName);
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			String uploadDir = UPLOAD_BASE_DIR + "/" + savedUser.getId();
			// 画像を更新する場合、既存の画像は削除
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			// 画像を必須にするのであれば、異なった処理が必要
			service.save(user);
		}
		redirectAttributes.addFlashAttribute("msg", "The user has been saved successfully");

		return getRedirectLinkUpdateUserInfo(user);
	}

	private String getRedirectLinkUpdateUserInfo(User user) {
		// 保存後、自分の情報だけを表示する方がUX向上！
		String firstPartOfEmail = user.getEmail().split("@")[0];
		// keyword : 検索フォームに入力する値（e-mailはユニーク、一人しか表示されないことが確約される）
		String pathForUpdateUser = "/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
		return "redirect:/users" + pathForUpdateUser; // usersのトップページにリダイレクト（再送信を防ぐ目的もあり）
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			User user = service.getUserById(id);
			List<Role> roles = service.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("roles", roles);
			model.addAttribute("pageTitle", "Edit User (ID:" + id + ")");
			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("msg", ex.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);

	}

}
