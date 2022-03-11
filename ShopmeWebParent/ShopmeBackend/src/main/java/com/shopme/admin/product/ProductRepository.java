package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	Product findByName(String name);

	Product findByAlias(String alias);

	@Query("select p from Product p where p.name like %?1% " 
	+ "or p.shortDescription like %?1%"
	+ "or p.fullDescription like %?1%"
	+ "or p.brand.name like %?1%"
	+ "or p.category.name like %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);

}
