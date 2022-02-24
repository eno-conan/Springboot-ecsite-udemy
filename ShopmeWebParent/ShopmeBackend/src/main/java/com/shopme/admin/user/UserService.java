package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	public static final int USERS_PER_PAGE = 4;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User getUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}

	public List<User> listAll() {
		// userRepo.findAll() : <Iterable> User -> Cast List User
		// 引数追加しているが、Controller側で追加する必要はない
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}

	public Page<User> listAllByPage(int pageNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);

		sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
		if (keyword != null) {
			return userRepo.findAll(keyword, pageable);
		}
		return userRepo.findAll(pageable);
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		// 新規か更新か処理を分ける
		boolean isUpdatingUser = (user.getId() != null);

		// idからUser情報取得
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();

//			更新時、パスワードを入力していない場合は、現在のパスワードで更新（何もしない）
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
			// 画像を更新しない場合、現状使用している画像のファイル名を保持してDB保存
			// ->formのhiddenがphotosの値をもたせればokか
//			user.setPhotos(existingUser.getPhotos());

		} else {
			encodePassword(user);
		}
		User savedInfo = userRepo.save(user);
		if (savedInfo == null) {
			System.out.println("not registered any user info");
			return null;
		}
		return savedInfo;
	}

	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findById(userInForm.getId()).get();

		if (!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}

		if (userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}

		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		return userRepo.save(userInDB);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);

		// まだ使用されていないアドレス：OK
		if (userByEmail == null) {
			return true;
		}

		// メールアドレスを変更しない場合の対応（DBと入力値の整合性が取れれば）
		if (userByEmail.getId() == id && userByEmail.getEmail().equals(email)) {
			return true;
		}

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			// 新規ユーザー
			if (userByEmail != null) {
				return false;
			}
		} else {
			// 既存User更新：メールアドレスから取得したidとパラメータで取得したＩＤは一致しないとおかしい
			if (userByEmail.getId() != id) {
				return false;
			}
		}

		return userByEmail == null;
	}

	public User getUserById(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with id" + id);
		}
	}

}
