package com.poly.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paymentID;

	@OneToOne
	@JoinColumn(name = "OrderID", nullable = false, unique = true)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "PaymentMethodID", nullable = false)
	private PaymentMethod paymentMethod;

	private LocalDateTime paymentDate;

	@Column(nullable = false)
	private String paymentStatus;

	private String transactionID;

	@Column(length = 500)
	private String paymentNote;
}
