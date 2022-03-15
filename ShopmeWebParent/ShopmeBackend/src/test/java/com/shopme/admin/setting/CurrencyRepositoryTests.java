package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Currency;

@DataJpaTest(showSql = false) // falseにしておくと、結果が見やすい
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {

	@Autowired
	private CurrencyRepository repo;

	@Test
	public void testCreateCurrecies() {
		Currency usd = new Currency("United States Dollar", "$", "USD");
		Currency gbp = new Currency("British Pound", "£", "GBP");
		Currency jpy = new Currency("Japanese", "¥", "JPY");
		Currency eur = new Currency("Euro", "€", "EUR");
		Currency rub = new Currency("Russian Ruble", "R", "RUB");
		Currency krw = new Currency("South Koren Won", "W", "KRW");
		Currency cny = new Currency("Chinese yuan", "¥", "CNY");
		Currency brl = new Currency("Brazilian Real", "R$", "BRL");
		Currency aud = new Currency("Australi	an Dollar", "$", "AUD");
		Currency cad = new Currency("Canadian Dollar", "$", "CAD");
		Currency vnd = new Currency("Vietnamese", "₫", "VND");
		Currency inr = new Currency("Indian Ruppe", "Rs", "INR");

		repo.saveAll(List.of(usd, gbp, jpy, eur, rub, krw, cny, brl, aud, cad, vnd, inr));
	}

	@Test
	public void testFindAllByOrderByNameAsc() {
		List<Currency> currencies = repo.findAllByOrderByNameAsc();

		assertThat(currencies.size()).isGreaterThan(0);
	}

}
