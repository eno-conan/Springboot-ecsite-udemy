package com.shopme.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// この設定を実装しないと、画像ファイルが読み込みできないようになっている:configure(HttpSecurity http)メソッドの実装により
		// webjars:bootstrapの読み込みで必要→Spring Securityの影響で全てのフォルダでやらないとエラー
		web.ignoring().antMatchers("/images/**", "/js/**", "/fontawesome/**", "/css/**", "/webjars/**", "/richtext/**");
	}

}
