package com.shopme;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

	@Query("select c from Category c where c.enabled = true order by c.name asc")
	public List<Category> findAllEnabled();

}
