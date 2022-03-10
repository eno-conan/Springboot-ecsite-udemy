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

}
