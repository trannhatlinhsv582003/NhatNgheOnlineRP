// PaymentService.java
package com.poly.Service;

import java.util.List;
import java.util.Optional;

import com.poly.Model.Order;
import com.poly.Model.Payment;

public interface PaymentService {
	List<Payment> findAll();

	Optional<Payment> findById(Integer id);

	Payment findByOrder(Order order);

	Payment save(Payment payment);

	void deleteById(Integer id);
}
