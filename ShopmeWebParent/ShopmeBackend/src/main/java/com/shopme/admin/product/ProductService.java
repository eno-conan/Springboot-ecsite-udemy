package com.shopme.admin.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.shopme.common.entity.Product;

@Service
public class ProductService {

	public static final int CATEGORIES_PER_PAGE = 4;

	@Autowired
	private ProductRepository repo;

	public List<Product> listAll() {
		List<Product> products = (List<Product>) repo.findAll();
		return products;
	}

	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null | product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());

		return repo.save(product);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);

		Product productByName = repo.findByName(name);
		if (isCreatingNew) {
			if (productByName != null) {
				return "Duplicated";
			}
		}
		return "OK";
	}
}
//	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
//		List<Category> hierarchicalCategories = new ArrayList<>();
//		for (Category category : rootCategories) {
//			hierarchicalCategories.add(Category.copyFull(category));
//			Set<Category> children = sortSubCategories(category.getChildren());
//			for (Category subCategory : children) {
//				String name = "--" + subCategory.getName();
//				hierarchicalCategories.add(Category.copyFull(subCategory, name));
//				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
//			}
//
//		}
//
//		return hierarchicalCategories;
//	}
//
//	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
//		int newSubLevel = subLevel + 1;
//		Set<Category> children = sortSubCategories(parent.getChildren());
//
//		for (Category subCategory : children) {
//			String name = "";
//			for (int i = 0; i < newSubLevel; i++) {
//				name += "--";
//			}
//			name += subCategory.getName();
//			hierarchicalCategories.add(Category.copyFull(subCategory, name));
//			// recursive
//			listSubCategoriesUsedInForm(hierarchicalCategories, subCategory, newSubLevel);
//		}
//	}
//
//	public Category save(Category category) {
//		return repo.save(category);
//	}
//
//	public List<Category> listCategoriesUsedInForm() {
//		List<Category> listCategoriesUsedInForm = new ArrayList<>();
//		Iterable<Category> categoriesInDB = repo.findAll(Sort.by("name").ascending());
//
//		for (Category category : categoriesInDB) {
//			// in case check "root category", add List all sub categories
//			if (category.getParent() == null) {
//				listCategoriesUsedInForm.add(Category.copyIdAndName(category));
//
//				Set<Category> children = sortSubCategories(category.getChildren());
//
//				for (Category subCategory : children) {
//					String name = "--" + subCategory.getName();
//					listCategoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
//					int subDirCnt = 1;
//					listSubCategoriesUsedInForm(listCategoriesUsedInForm, subCategory, subDirCnt);
//				}
//
//			}
//		}
//		return listCategoriesUsedInForm;
//	}
//
//	private void listSubCategoriesUsedInForm(List<Category> listCategoriesUsedInForm, Category parent, int subLevel) {
//		int newSubLevel = subLevel + 1;
//		Set<Category> children = sortSubCategories(parent.getChildren());
//
//		for (Category subCategory : children) {
//			String name = "";
//			for (int i = 0; i < newSubLevel; i++) {
//				name += "--";
//			}
//			name += subCategory.getName();
//			listCategoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
//			// recursive
//			listSubCategoriesUsedInForm(listCategoriesUsedInForm, subCategory, newSubLevel);
//		}
//
//	}
//
//	public String checkUnique(Integer id, String name, String alias) {
//		boolean isCreatingNew = (id == null || id == 0);
//
//		Category categoryByName = repo.findByName(name);
//		if (isCreatingNew) {
//			if (categoryByName != null) {
//				return "DuplicatedName";
//			} else {
//				Category categoryByAlias = repo.findByAlias(alias);
//				if (categoryByAlias != null) {
//					return "DuplicatedAlias";
//				}
//			}
//		}
//		return "OK";
//	}
//
//	public Page<Category> listAllByPage(int pageNum, String sortField, String sortDir, String keyword) {
//		Sort sort = Sort.by(sortField);
//
//		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();
//
//		Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE, sort);
//		if (keyword != null) {
//			return repo.findAll(keyword, pageable);
//		}
//		return repo.findAll(pageable);
//	}
//
//	private SortedSet<Category> sortSubCategories(Set<Category> children) {
//		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
//
//			@Override
//			public int compare(Category cat1, Category cat2) {
//				return cat1.getName().compareTo(cat2.getName());
//			}
//
//		});
//		sortedChildren.addAll(children);
//		return sortedChildren;
//	}
