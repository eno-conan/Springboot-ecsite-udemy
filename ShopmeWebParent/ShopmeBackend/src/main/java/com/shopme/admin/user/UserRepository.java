package com.shopme.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	@Query("select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email") String email);

	// user filter function
	@Query("select u from User u where concat(u.id,' ',u.email,' ',u.firstName,' ',u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);

}
