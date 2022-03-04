package com.shopme.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, length = 256, nullable = false)
	private String name;

	@Column(length = 256, nullable = false)
	private String alias;

	@Column(name = "short_description", length = 512, nullable = false)
	private String shortDescription;

	@Column(name = "full_description", length = 4096, nullable = false)
	private String fullDescription;

	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "updated_time")
	private Date updatedTime;

	private boolean enabled;

	@Column(name = "in_stock")
	private boolean inStock;

	private float cost;
	private float price;

	@Column(name = "discount_percent")
	private float discountPercent;

	private float length;
	private float width;
	private float height;
	private float weight;

	@Column(name = "main_image", nullable = false)
	private String mainImage;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Set<ProductImage> images = new HashSet<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Set<ProductDetail> details = new HashSet<>();

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}

	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName, this));
	}

	public String getMainImagePath() {
		if ("image-thumbnail.png".equals(this.mainImage) || id == null) {
			return "/images/image-thumbnail.png";
		} else {
			return "/product-images/" + this.id + "/" + this.mainImage;
		}
	}

	public void addDetail(String name, String value) {
		this.details.add(new ProductDetail(name, value, this));
	}

}
