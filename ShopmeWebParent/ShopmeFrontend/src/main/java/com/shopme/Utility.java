package com.shopme;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.setting.EmailSettingBag;

public class Utility {

	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}	

	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUserName());
		mailSender.setPassword(settings.getPassword());

		Properties mailPropeties = new Properties();
		mailPropeties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailPropeties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

		mailSender.setJavaMailProperties(mailPropeties);

		return mailSender;
	}

}
