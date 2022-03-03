package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest(showSql = false) // falseにしておくと、結果が見やすい
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 2);
		Category category = entityManager.find(Category.class, 10);

		Product product = new Product();
		product.setName("Test Product");
		product.setAlias("Test Product alias");
		product.setShortDescription("short description for test product");
		product.setFullDescription("full description for test product");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(1500);
		product.setCost(200);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = repo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repo.findAll();
		iterableProducts.forEach(p -> System.out.println(p));
	}

	@Test
	public void testGetProduct() {
		Product testProduct = repo.findById(1).get();

		assertThat(testProduct.getId()).isEqualTo(1);
		assertThat(testProduct.getName()).isEqualTo("Test Product");
	}

	@Test
	public void testUpdateProduct() {
		Product testUpdateProduct = repo.findById(2).get();
		testUpdateProduct.setName("Test Product2 Updated");

		Product savedProduct = repo.save(testUpdateProduct);

		assertThat(savedProduct.getId()).isEqualTo(2);
		assertThat(savedProduct.getName()).isEqualTo("Test Product2 Updated");
	}

	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = repo.findById(productId).get();

		product.setMainImage("mainImage.jpg");
		product.addExtraImage("extra1.jpg");
		product.addExtraImage("extra2.jpg");
		product.addExtraImage("extra3.jpg");

		Product savedProduct = repo.save(product);
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}

}
