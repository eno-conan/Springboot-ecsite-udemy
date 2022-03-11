package com.shopme.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.CategoryRepository;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	public List<Category> listNoChildrenCategories() {
		List<Category> listNoChildrenCategories = new ArrayList<>();
		List<Category> listEnabledCategories = repo.findAllEnabled();

		listEnabledCategories.forEach(ctgr -> {
			Set<Category> children = ctgr.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategories.add(ctgr);
			}
		});

		return listNoChildrenCategories;
	}

	public Category getCategory(String alias) {
		return repo.findByAliasEnabled(alias);
	}

	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>();

		Category parent = child.getParent();
		// retrieve parent category name
		// child -> parent -> grandparent...
		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}

		listParents.add(child);

		return listParents;

	}

}
