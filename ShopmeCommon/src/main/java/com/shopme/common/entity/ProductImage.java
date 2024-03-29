package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_images")
public class ProductImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	public ProductImage() {

	}

	public ProductImage(Integer id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}

	public ProductImage(String imageName, Product product) {
		this.name = imageName;
		this.product = product;
	}

	@ManyToOne // one product has many pictures.
	@JoinColumn(name = "product_id")
	private Product product;

	@Transient
	public String getImagePath() {
		return "/product-images/" + this.product.getId() + "/extras/" + this.name;
	}

}
