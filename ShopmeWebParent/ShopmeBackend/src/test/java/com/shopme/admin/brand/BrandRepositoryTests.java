package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false) // falseにしておくと、結果が見やすい
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

	@Autowired
	private BrandRepository repo;

	@Test
	public void createBrand1() {
		Category laptops = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);

		Brand savedBrand1 = repo.save(acer);

		assertThat(savedBrand1).isNotNull();
		assertThat(savedBrand1.getId()).isGreaterThan(0);
	}

	@Test
	public void createBrand2() {
		Brand apple = new Brand("Apple");
		Category cellphones = new Category(4);
		Category tablets = new Category(7);
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablets);

		Brand savedBrand2 = repo.save(apple);

		assertThat(savedBrand2).isNotNull();
		assertThat(savedBrand2.getId()).isGreaterThan(0);
	}

	@Test
	public void findAllBrands() {
		List<Brand> brands = (List<Brand>) repo.findAll();
//		brands.forEach(System.out::println);
		brands.forEach(brand -> {
			System.out.println(brand.getId() + " / " + brand.getName());
			brand.getCategories().forEach(category -> System.out.println(category.getName()));
		});

		assertThat(brands).isNotEmpty();

	}

	@Test
	public void findBrandById() {
		Brand brands = repo.findById(1).get();

		assertThat(brands.getId()).isEqualTo(1);
		assertThat(brands.getName()).isEqualTo("Acer");

	}

	@Test
	public void updateBrandName() {
		Brand brands = repo.findById(3).get();
		brands.setName("UpdateTest");

		Brand savedBrands = repo.save(brands);

		assertThat(savedBrands.getName()).isEqualTo("UpdateTest");

	}

}
