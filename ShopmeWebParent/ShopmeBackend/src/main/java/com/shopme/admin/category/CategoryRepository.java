package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

	@Query("select c FROM Category c where c.parent.id is null")
	public List<Category> findRootCategories();

	public Category findByName(String name);
	public Category findByAlias(String name);

	@Query("select c from Category c where concat(c.id,' ',c.name,' ',c.alias) LIKE %?1%")
	public Page<Category> findAll(String keyword, Pageable pageable);

}
