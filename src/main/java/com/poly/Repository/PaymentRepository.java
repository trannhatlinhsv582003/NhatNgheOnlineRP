// PaymentRepository.java
package com.poly.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.Order;
import com.poly.Model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	Payment findByOrder_OrderID(Integer orderId);

	Payment findByOrder(Order order);
}
