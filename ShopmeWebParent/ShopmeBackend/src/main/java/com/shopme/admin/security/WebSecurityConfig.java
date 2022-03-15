package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 認証処理を担う
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		// ユーザー名（本サイトではメールアドレスで行う）による認証処理（情報検索）
		authProvider.setUserDetailsService(userDetailsService());
		// パスワード暗号化
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;

	}

	// ProviderManager（司令塔）
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

//	以下メールアドレスとパスワードでテストログイン可能
//	all@gmail.com:987654321
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/users/**").hasAuthority("Admin")
				.antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
				.antMatchers("/products/new", "/products/delete").hasAnyAuthority("Admin","Editor")
				.antMatchers("/products/edit/**", "/products/save","/products/check_unique").hasAnyAuthority("Admin", "SalesPerson", "Editor")
				.antMatchers("/products", "/products/","/products/detail/**","/products/page/**").hasAnyAuthority("Admin", "SalesPerson", "Editor", "Shipper")
				.antMatchers("/products/**").hasAnyAuthority("Admin","Editor")
				.antMatchers("/settings/**").hasAuthority("Admin").anyRequest().authenticated()
				.and().formLogin().loginPage("/login").usernameParameter("email").permitAll().and()
				.logout().permitAll().and().rememberMe().key("Abcdefghijkl_123456789")
				.tokenValiditySeconds(60 * 60 * 24 * 7);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// この設定を実装しないと、画像ファイルが読み込みできないようになっている:configure(HttpSecurity http)メソッドの実装により
		// webjars:bootstrapの読み込みで必要→Spring Securityの影響で全てのフォルダでやらないとエラー
		web.ignoring().antMatchers("/images/**", "/js/**", "/fontawesome/**", "/css/**", "/webjars/**", "/richtext/**");
	}

}
