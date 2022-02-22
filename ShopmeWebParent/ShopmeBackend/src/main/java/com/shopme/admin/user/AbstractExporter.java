package com.shopme.admin.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {

	public void setResponseHeader(HttpServletResponse response, String contentType, String extension) {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormatter.format(new Date());
		String fileName = "users_" + timestamp + extension;

		response.setContentType(contentType);

		String headerKey = "Content-Disposition"; // ←なんだこれ？
		String headerValue = "attachment; fileName=" + fileName;// ←なんだこれ？
		response.setHeader(headerKey, headerValue);

	}

}
