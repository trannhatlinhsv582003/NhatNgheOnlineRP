package com.poly.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PaymentMethods")
public class PaymentMethod {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer methodID;

	@Column(nullable = false, unique = true)
	private String code;

	@Column(nullable = false)
	private String name;

	private String description;
	private String iconURL;
}