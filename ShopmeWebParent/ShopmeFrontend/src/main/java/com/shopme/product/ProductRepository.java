package com.shopme.product;

import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	@Query("select p from Product p where p.enabled = true"
			+ " and (p.category.id = ?1 or p.category.allParentIDs like %?2%)" + " order by p.name asc")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);
	
	public Product findByAlias(String alias);

	@Query(value="select * from products as p where p.enabled = true and "
			+ "MATCH(p.name,p.short_description,p.full_description) against (?1)",
			nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);

}
