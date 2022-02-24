package com.shopme.admin.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.User;

@Controller
public class AccountController {

	public static final String UPLOAD_BASE_DIR = "user-photos";

	@Autowired
	private UserService service;

	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {
		String email = loggedUser.getUsername();
		User user = service.getUserByEmail(email);
		model.addAttribute("user", user);
		return "account_form";
	}

	@PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		// @AuthenticationPrincipal ShopmeUserDetails loggedUser: related
		// ShopmeUserDetail class.

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());// \と/の違いを吸収するっぽい
			System.out.println(fileName);
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			String uploadDir = UPLOAD_BASE_DIR + "/" + savedUser.getId();
			// 画像を更新する場合、既存の画像は削除
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			// 画像を必須にするのであれば、異なった処理が必要
			service.updateAccount(user);
		}

		// update userName at menu-bar
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		redirectAttributes.addFlashAttribute("msg", "Your Account details have been updated.");

		return "redirect:/account";
	}
}
