package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepo;

	public List<Setting> listAllSettings() {
		return (List<Setting>) settingRepo.findAll();
	}

	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings = new ArrayList<>();

		List<Setting> generalSettings = settingRepo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepo.findByCategory(SettingCategory.CURRENCY);

		settings.addAll(generalSettings);
		settings.addAll(currencySettings);

		return new GeneralSettingBag(settings);

	}

	public void saveAll(Iterable<Setting> settings) {
		settingRepo.saveAll(settings);
	}

	public List<Setting> getMailServerSettings() {
		return settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
	}

	public List<Setting> getMailTemplateSettings() {
		return settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATE);
	}
}