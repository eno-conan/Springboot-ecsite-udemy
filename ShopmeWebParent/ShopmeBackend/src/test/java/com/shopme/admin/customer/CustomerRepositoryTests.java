package com.shopme.admin.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest // falseにしておくと、結果が見やすい
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void testCreateCustomer() {
		Customer customer = generateCustomerData();
		Customer savedCustomer = customerRepo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		Customer retrieveCustomer = customerRepo.findById(customerId).get();
		updateCustomerInfo(retrieveCustomer);
		Customer savedCustomer = customerRepo.save(retrieveCustomer);

		assertThat(savedCustomer.getId()).isGreaterThan(0);
		assertThat(savedCustomer.getEmail()).isEqualTo("updateCustomer@gmail.com");
		assertThat(savedCustomer.getCountry().getId()).isEqualTo(2);

	}

	private void updateCustomerInfo(Customer retrieveCustomer) {
		Integer countryId = 2;// United-Arab-Emirates.
		Country Updatecountry = testEntityManager.find(Country.class, countryId);
		retrieveCustomer.setEmail("updateCustomer@gmail.com");
		retrieveCustomer.setPassword("updatePassword");
		retrieveCustomer.setLastName("updateLast");
		retrieveCustomer.setFirstName("updateFirst");
		retrieveCustomer.setPhoneNumber("111-1111-1111");
		retrieveCustomer.setPostalCode("765-4321");
		retrieveCustomer.setAddressLine1("NewYork");
		retrieveCustomer.setAddressLine2("XXStreet");
		retrieveCustomer.setCity("updateCity");
		retrieveCustomer.setState("updateState");
		retrieveCustomer.setCountry(Updatecountry);
		retrieveCustomer.setCreatedTime(new Date());
	}

	@Test
	public void testFindByEmail() {
		String emailString = "updateCustomer@gmail.com";
		Customer customer = customerRepo.findByEmail(emailString);
		assertThat(customer).isNotNull();
//		assertEquals(customer.getLastName(), "updateLast");
	}

	private Customer generateCustomerData() {
		Customer testCustomer = new Customer();

		Integer countryId = 1;// Andorra
		Country country = testEntityManager.find(Country.class, countryId);

		testCustomer.setEmail("testCustomer@gmail.com");
		testCustomer.setPassword("password");
		testCustomer.setLastName("last");
		testCustomer.setFirstName("first");
		testCustomer.setPhoneNumber("000-0000-0000");
		testCustomer.setPostalCode("123-4567");
		testCustomer.setAddressLine1("Tokyo");
		testCustomer.setAddressLine2("Sumida");
		testCustomer.setCity("City");
		testCustomer.setState("State");
		testCustomer.setCountry(country);
		testCustomer.setCreatedTime(new Date());

		return testCustomer;
	}

}
