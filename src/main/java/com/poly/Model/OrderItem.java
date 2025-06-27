package com.poly.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "OrderItems")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderItemID;

	@ManyToOne
	@JoinColumn(name = "OrderID", nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "ProductID", nullable = false)
	private Product product;

	private Integer quantity;
	private BigDecimal price;
}
