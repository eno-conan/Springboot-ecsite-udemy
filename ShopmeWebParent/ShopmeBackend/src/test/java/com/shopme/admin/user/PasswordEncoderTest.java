package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void testEncodeTest() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPass = "password";
		String encodePassword = passwordEncoder.encode(rawPass);

		System.out.println(encodePassword);

		boolean matches = passwordEncoder.matches(rawPass, encodePassword);

		assertThat(matches).isTrue();

	}

}
