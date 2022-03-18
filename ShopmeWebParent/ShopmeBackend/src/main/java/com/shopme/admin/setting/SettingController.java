package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
	private CurrencyRepository currencyRepo;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listsSettings = settingService.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listsSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "settings/settings";
	}

	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

		GeneralSettingBag settingBag = settingService.getGeneralSettings();
		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);
		updateSettingValuesFromForm(request, settingBag.getListSettings());

		redirectAttributes.addFlashAttribute("msg", "General Setting have been saved successfully");
		return "redirect:/settings";

	}

	// these private method will fix at another class?
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String fileNameFilledBySnake = fileName.replace(" ", "_");
			String value = "/site-logo/" + fileNameFilledBySnake;
			settingBag.updateSiteLogo(value);

			String uploadDir = UPLOAD_BASE_DIR;

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileNameFilledBySnake, multipartFile);
		}
	}

	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);

		if (findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}

	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSetting) {
		for (Setting setting : listSetting) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		settingService.saveAll(listSetting);
	}

}
