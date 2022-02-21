package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false) // falseにしておくと、結果が見やすい
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("test@test.co.jp", "password", "test", "jose");
		user.addRole(roleAdmin);

		User savedUser = repo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		User user = new User("test2@test.co.jp", "password2", "test2", "Cablera");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		user.addRole(roleEditor);
		user.addRole(roleAssistant);

		User savedUser = repo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User getUser = repo.findById(1).get();
		System.out.println(getUser);
		assertThat(getUser).isNotNull();
	}

	@Test
	public void testUpdateUserDetail() {
		User user = repo.findById(1).get();
		user.setEnabled(true);
		user.setEmail("update@gmail.com");

		repo.save(user);
	}

	@Test
	public void testUpdateUserRole() {
		User user = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);

		user.getRoles().remove(roleEditor);
		user.addRole(roleSalesperson);

		repo.save(user);
	}

	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}

	@Test
	public void testGetUserByEmail() {
		String email = "update@gmail.com";// DBに存在するemailであればテスト成功
		User user = repo.getUserByEmail(email);

		assertThat(user).isNotNull();
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;

		// ページングで設定した件数分が取得できているか検証
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUsers() {
		String keyword = "abc";

		int pageNumber = 0;
		int pageSize = 4;

		// ページングで設定した件数分が取得できているか検証
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isGreaterThan(0);

	}

}
