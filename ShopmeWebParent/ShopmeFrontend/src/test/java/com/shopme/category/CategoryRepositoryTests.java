package com.shopme.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.CategoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false) // falseにしておくと、結果が見やすい
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository repo;

	@Test
	public void testListEnabledCategories() {
		List<Category> categories = repo.findAllEnabled();
		categories.forEach(ctgr -> {
			System.out.println(ctgr.getName() + "(" + ctgr.isEnabled() + ")");
		});
	}

	@Test
	public void testFindByAliasEnabled() {
		String alias = "Desktops";
		Category category = repo.findByAliasEnabled(alias);
		assertThat(category.isEnabled()).isEqualTo(true);
	}
}
