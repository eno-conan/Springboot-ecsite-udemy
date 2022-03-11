package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shopme.common.entity.Brand;

@Service
public class BrandService {

	public static final int CATEGORIES_PER_PAGE = 4;

	@Autowired
	private BrandRepository repo;

	public List<Brand> listAll() {
		List<Brand> brands = (List<Brand>) repo.findAll();
		return brands;
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);

		Brand brandByName = repo.findByName(name);
		if (isCreatingNew) {
			if (brandByName != null) {
				return "Duplicated";
			}
		}
		return "OK";
	}

	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	public Brand get(Integer brandId) throws BrandNotFoundException {
		try {
			return repo.findById(brandId).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("could not find any brand with id " + brandId);
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
}
