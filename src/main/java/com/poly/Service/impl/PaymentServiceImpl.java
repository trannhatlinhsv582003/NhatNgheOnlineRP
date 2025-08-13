// PaymentServiceImpl.java
package com.poly.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.Model.Order;
import com.poly.Model.Payment;
import com.poly.Repository.PaymentRepository;
import com.poly.Service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentRepository repo;

	public List<Payment> findAll() {
		return repo.findAll();
	}

	public Optional<Payment> findById(Integer id) {
		return repo.findById(id);
	}

	public Payment save(Payment payment) {
		return repo.save(payment);
	}

	@Override
	@Transactional(readOnly = true)
	public Payment findByOrder(Order order) {
		return repo.findByOrder(order);
	}

	public void deleteById(Integer id) {
		repo.deleteById(id);
	}
}
