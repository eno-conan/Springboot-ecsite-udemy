package com.shopme.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 64)
	private String name;

	@Column(nullable = false, length = 5)
	private String code;

	@OneToMany(mappedBy = "country")
	private Set<State> states;

	public Country() {

	}

	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Country(String countryName) {
		this.name = countryName;
	}

	public Country(Integer countryId) {
		this.id = countryId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

}
