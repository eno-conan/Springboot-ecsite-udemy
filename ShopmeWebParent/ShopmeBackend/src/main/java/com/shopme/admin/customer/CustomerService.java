package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private CountryRepository countryRepo;

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

}
