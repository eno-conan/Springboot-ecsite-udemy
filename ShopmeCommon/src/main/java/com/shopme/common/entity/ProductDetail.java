package com.shopme.common.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_details")
public class ProductDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 255, nullable = false)
	private String name;

	@Column(length = 256, nullable = false)
	private String value;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductDetail() {
	}

	public ProductDetail(String name, String value, Product product) {
		this.name = name;
		this.value = value;
		this.product = product;
	}

}
