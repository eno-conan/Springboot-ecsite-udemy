package com.shopme.admin.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	Product findByName(String name);

	Product findByAlias(String alias);

}
