package com.shopme.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(@Param("email") String email) {
		return customerService.isEmailUnique(email) ? "OK" : "Duplicated";
	}

}
