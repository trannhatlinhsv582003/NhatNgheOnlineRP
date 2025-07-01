package com.poly.Service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.Model.PaymentMethod;
import com.poly.Repository.PaymentMethodRepository;
import com.poly.Service.PaymentMethodService;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

	@Autowired
	private PaymentMethodRepository paymentMethodRepo;

	@Override
	public List<PaymentMethod> findAll() {
		return paymentMethodRepo.findAll();
	}

	@Override
	public PaymentMethod findById(Integer id) {
		return paymentMethodRepo.findById(id).orElse(null);
	}

	@Override
	public PaymentMethod findByCode(String code) {
		return paymentMethodRepo.findByCode(code);
	}

	@Override
	public PaymentMethod save(PaymentMethod method) {
		return paymentMethodRepo.save(method);
	}

	@Override
	public void deleteById(Integer id) {
		paymentMethodRepo.deleteById(id);
	}
}
