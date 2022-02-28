package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Service
public class CategoryService {

	public static final int CATEGORIES_PER_PAGE = 4;

	@Autowired
	private CategoryRepository repo;

	public List<Category> listAll() {
		List<Category> rootCategories = repo.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		for (Category category : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(category));
			Set<Category> children = category.getChildren();
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			}

		}

		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			// recursive
			listCategoriesUsedInForm(hierarchicalCategories, subCategory, newSubLevel);
		}
	}

	public Category save(Category category) {
		return repo.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> listCategoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = repo.findAll();

		for (Category category : categoriesInDB) {
			// in case check "root category", add List all sub categories
			if (category.getParent() == null) {
				listCategoriesUsedInForm.add(Category.copyIdAndName(category));

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					listCategoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), subCategory.getName()));
					int subDirCnt = 1;
					listCategoriesUsedInForm(listCategoriesUsedInForm, subCategory, subDirCnt);
				}

			}
		}
		return listCategoriesUsedInForm;
	}

	private void listCategoriesUsedInForm(List<Category> listCategoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			listCategoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), subCategory.getName()));
			// recursive
			listCategoriesUsedInForm(listCategoriesUsedInForm, subCategory, newSubLevel);
		}

	}

	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);

		Category categoryByName = repo.findByName(name);
		if (isCreatingNew) {
			if (categoryByName != null) {
				return "DuplicatedName";
			} else {
				Category categoryByAlias = repo.findByAlias(alias);
				if (categoryByAlias != null) {
					return "DuplicatedAlias";
				}
			}
		}
		return "OK";
	}

	public Page<Category> listAllByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);

		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE, sort);
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		return repo.findAll(pageable);
	}

}