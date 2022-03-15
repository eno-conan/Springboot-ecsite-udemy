package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

@Controller
public class SettingController {

	public static final String UPLOAD_BASE_DIR = "../site-logo/";

	@Autowired
	private SettingService settingService;

	@Autowired
	private CurrencyRepository currencyRepository;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listsSettings = settingService.listAllSettings();
		List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listsSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "settings/settings";
	}

	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String fileNameFilledBySnake = fileName.replace(" ", "_");
			String uploadDir = UPLOAD_BASE_DIR;

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileNameFilledBySnake, multipartFile);
		}
		return "redirect:/settings";

	}

}
