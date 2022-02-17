package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("test@test.co.jp", "password", "test", "jose");
		user.addRole(roleAdmin);

		User savedUser = userRepo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);

	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User user = new User("test2@test.co.jp", "password2", "test2", "Cablera");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		user.addRole(roleEditor);
		user.addRole(roleAssistant);

		User savedUser = userRepo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);

	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers =  userRepo.findAll();
		listUsers.forEach(user->System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User getUser = userRepo.findById(1).get();
		System.out.println(getUser);
		assertThat(getUser).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetail() {
		User user = userRepo.findById(1).get();
		user.setEnabled(true);
		user.setEmail("update@gmail.com");
		
		userRepo.save(user);
	}
	
	@Test
	public void testUpdateUserRole() {
		User user = userRepo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		user.getRoles().remove(roleEditor);
		user.addRole(roleSalesperson);
		
		userRepo.save(user);
	}

}
