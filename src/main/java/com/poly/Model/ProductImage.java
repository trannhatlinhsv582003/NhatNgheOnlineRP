package com.poly.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ProductImages")
@Data
public class ProductImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ImageID")
	private Integer imageID;

	@ManyToOne
	@JoinColumn(name = "ProductID", nullable = false)
	private Product product;

	@Column(name = "ImageURL", nullable = false)
	private String imageURL;
}
