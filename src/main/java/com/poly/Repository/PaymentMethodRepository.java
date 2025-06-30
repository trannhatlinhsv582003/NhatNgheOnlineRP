package com.poly.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

	// Optional: tìm theo code nếu cần
	PaymentMethod findByCode(String code);
}
