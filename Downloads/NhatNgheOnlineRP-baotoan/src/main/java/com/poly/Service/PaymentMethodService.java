package com.poly.Service;

import java.util.List;

import com.poly.Model.PaymentMethod;

public interface PaymentMethodService {

	List<PaymentMethod> findAll();

	PaymentMethod findById(Integer id);

	PaymentMethod findByCode(String code);

	PaymentMethod save(PaymentMethod method);

	void deleteById(Integer id);
}
