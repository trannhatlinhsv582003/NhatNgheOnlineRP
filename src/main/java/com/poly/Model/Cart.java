package com.poly.Model;

import java.time.LocalDateTime;

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
@Table(name = "Carts")
@Data
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CartID")
	private Integer cartID;

	@ManyToOne
	@JoinColumn(name = "UserID", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "ProductID", nullable = false)
	private Product product;

	@Column(name = "Quantity", nullable = false)
	private Integer quantity;

	@Column(name = "AddedAt")
	private LocalDateTime addedAt;
}
