package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false) // falseにしておくと、結果が見やすい
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository categoryRepo;

	@Test
	public void testCreateRootCategory() {
//		Category category = new Category("Computers");
		Category category = new Category("Electronics");
		Category savedCategory = categoryRepo.save(category);

		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(5);
		Category subCategory = new Category("Memory", parent);

		Category savedCategory = categoryRepo.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testGetCategory() {
		Category category = categoryRepo.findById(1).get();
		System.out.println(category.getName());

		Set<Category> children = category.getChildren();

		for (final Category subCategory : children) {
			System.out.println(subCategory.getName());
		}

		assertThat(children.size()).isGreaterThan(0);

	}

	@Test
	public void testListRootCategories() {
		List<Category> categories = categoryRepo.findRootCategories(Sort.by("name").ascending());
		categories.forEach(ctgr -> System.out.println(ctgr.getName()));
	}

	@Test
	public void testFindByName() {
		String name = "Computers";
		Category category = categoryRepo.findByName(name);

		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);

	}
	
	@Test
	public void testFindByAlias() {
		String alias = "Computers";
		Category category = categoryRepo.findByAlias(alias);

		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);

	}

}
